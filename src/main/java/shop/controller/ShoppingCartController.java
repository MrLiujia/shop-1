package shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import shop.model.ShoppingCart;
import shop.service.ShoppingCartService;

@Controller
public class ShoppingCartController extends BaseController {
    private ShoppingCartService shoppingCartService;
    
    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/uc/shopping-cart/add")
    public String add(@RequestParam Long cellphoneId,
                      @AuthenticationPrincipal(expression = "user.id") Long userId) {
        // 注意：SecurityConfig中配置的/uc/**需要登录保证了到达此控制器方法时，必定有用户已登录，这样才能顺利拿到userId
        shoppingCartService.addToCart(userId, cellphoneId, 1);
        return "redirect:/cellphones/" + cellphoneId;
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/uc/shopping-cart")
    public String details(@AuthenticationPrincipal(expression = "user.id") Long userId,
                          Model model) {
        ShoppingCart shoppingCart = shoppingCartService.findOneByUserId(userId);
        model.addAttribute("shoppingCart", shoppingCart);
        return "shopping-cart";
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/uc/shopping-cart/remove-item")
    public String removeItem(@AuthenticationPrincipal(expression = "user.id") Long userId,
                             @RequestParam Long cellphoneId) {
        shoppingCartService.removeItem(userId, cellphoneId);
        return "redirect:/uc/shopping-cart";
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/uc/shopping-cart/update-item-quantity")
    @ResponseBody // 把返回值作为响应内容，加了jackson库之后，会转换为json文本
    public ShoppingCart updateItemQuantity(@AuthenticationPrincipal(expression = "user.id") Long userId,
                                         @RequestParam Long cellphoneId,
                                         @RequestParam Integer quantity) {
        shoppingCartService.updateItemQuantity(userId, cellphoneId, quantity);
        return shoppingCartService.findOneByUserId(userId);
    }
}
