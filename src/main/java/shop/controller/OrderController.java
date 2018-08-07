package shop.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import shop.controller.form.OrderForm;
import shop.model.Order;
import shop.model.ShippingAddress;
import shop.model.ShoppingCart;
import shop.service.OrderService;
import shop.service.ShippingAddressService;
import shop.service.ShoppingCartService;

import com.alipay.api.AlipayApiException;

@Controller
public class OrderController extends BaseController {
    private ShoppingCartService shoppingCartService;
    
    private ShippingAddressService shippingAddressService;
    
    private OrderService orderService;
    
    @Autowired
    public OrderController(ShoppingCartService shoppingCartService,
                           ShippingAddressService shippingAddressService,
                           OrderService orderService) {
        this.shoppingCartService = shoppingCartService;
        this.shippingAddressService = shippingAddressService;
        this.orderService = orderService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/uc/orders/add")
    public String add(@AuthenticationPrincipal(expression = "user.id") Long userId,
                      Model model,
                      @ModelAttribute OrderForm orderForm) {
        prepareInAdd(userId, model);
        return "order-add";
    }

    private void prepareInAdd(Long userId, Model model) {
        ShoppingCart shoppingCart = shoppingCartService.findOneByUserId(userId);
        model.addAttribute("shoppingCart", shoppingCart);
        List<ShippingAddress> shippingAddresses = shippingAddressService.findAll(userId);
        model.addAttribute("shippingAddresses", shippingAddresses);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/uc/orders/add")
    public String create(@AuthenticationPrincipal(expression = "user.id") Long userId,
                         @ModelAttribute @Valid OrderForm orderForm,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            prepareInAdd(userId, model);
            return "order-add";
        }
        Order order = orderService.create(userId, orderForm.getShippingAddressId());
        return "redirect:/uc/orders/" + order.getId();
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/uc/orders/{id}")
    public String details(@AuthenticationPrincipal(expression = "user.id") Long userId,
                          @PathVariable Long id,
                          Model model) {
        Order order = orderService.findOne(userId, id);
        model.addAttribute("order", order);
        return "order-details";
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/uc/orders/")
    public String list(@AuthenticationPrincipal(expression = "user.id") Long userId,
                       Model model) {
        List<Order> orders = orderService.findAll(userId);
        model.addAttribute("orders", orders);
        return "order-list";
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/uc/orders/{id}/pay", 
                    produces = "text/html;charset=UTF-8" // 非常重要，支付宝api响应是html片段（不含编码），必须显式指定
                    )
    @ResponseBody
    public String pay(@AuthenticationPrincipal(expression = "user.id") Long userId,
                      @PathVariable Long id) throws AlipayApiException {
        return orderService.payForm(userId, id);
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/uc/orders/sync-pay-cb")
    public String payOk(@RequestParam("out_trade_no") String orderNumber,
                        @RequestParam Map<String, String> paramMap, // 将所有请求参数封装到map中
                        Model model) {
        // 验签
        orderService.verifySignature(paramMap);
        
        Long orderId = Long.valueOf(orderNumber.split("-")[0]); // 如 3-1533093080374
        model.addAttribute("orderId", orderId);
        return "order-pay-ok";
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/async-pay-cb")
    @ResponseBody // 响应内容是text/plain
    public String onPayResult(@RequestParam Map<String, String> paramMap) {
        orderService.handlePayResult(paramMap);
        return "success";
    }
}
