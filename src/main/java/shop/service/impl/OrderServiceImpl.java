package shop.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mapper.OrderMapper;
import shop.model.Order;
import shop.model.OrderItem;
import shop.model.OrderState;
import shop.model.ShippingAddress;
import shop.model.ShoppingCart;
import shop.model.ShoppingCartItem;
import shop.service.AlipayResultException;
import shop.service.AlipaySignatureException;
import shop.service.OrderService;
import shop.service.ShoppingCartService;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private OrderMapper orderMapper;
    
    private ShoppingCartService shoppingCartService;
    
    private AlipayClient alipayClient;
    
    private String alipayReturnUrl;
    private String alipayNotifyUrl;
    private String alipayPublicKey;
    private String alipaySignType;
    private String appId;
    
    private ObjectMapper objectMapper;
    
    @Autowired
    public OrderServiceImpl(OrderMapper orderMapper,
                            ShoppingCartService shoppingCartService, 
                            AlipayClient alipayClient,
                            Environment env,
                            ObjectMapper objectMapper) throws IOException {
        this.orderMapper = orderMapper;
        this.shoppingCartService = shoppingCartService;
        this.alipayClient = alipayClient;
        
        this.alipayReturnUrl = env.getProperty("alipay.returnUrl");
        this.alipayNotifyUrl = env.getProperty("alipay.notifyUrl");
        this.alipayPublicKey = FileUtils.readFileToString(
                new File(env.getProperty("alipay.alipayPublicKeyFile")), 
                "UTF-8");
        this.alipaySignType = env.getProperty("alipay.signType");
        this.appId = env.getProperty("alipay.appId");
        
        this.objectMapper = objectMapper;
    }

    @Override
    public Order create(Long userId, Long shippingAddressId) {
        // 订单表
        Order order = new Order();
        order.setUserId(userId);
        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setId(shippingAddressId);
        order.setShippingAddress(shippingAddress);
        order.setCreatedTime(new Date());
        order.setState(OrderState.Created);
        
        logger.debug("插入订单");
        
        orderMapper.create(order);
        
        // 订单项表
        ShoppingCart shoppingCart = shoppingCartService.findOneByUserId(userId);
        for (ShoppingCartItem cartItem : shoppingCart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setCellphone(cartItem.getCellphone());
            orderItem.setQuantity(cartItem.getQuantity());
            
            logger.debug("插入订单项");
            orderMapper.addItem(orderItem);
        }
        
        // 清空购物车
        logger.debug("清空购物车");
        shoppingCartService.clearCart(userId);
        
        logger.info("订单已创建: #" + order.getId());
        return order;
    }

    @Override
    public Order findOne(Long userId, Long orderId) {
        return orderMapper.findOne(userId, orderId);
    }

    @Override
    public List<Order> findAll(Long userId) {
        return orderMapper.findAll(userId);
    }

    @Override
    public String payForm(Long userId, Long id) {
        Order order = findOne(userId, id);
        
        if (order.getState() != OrderState.Created) {
            throw new IllegalStateException("只有Created状态的订单才能发起支付");
        }
        
        int totalAmountInFen = order.totalCost();
        BigDecimal totalAmount = BigDecimal.valueOf(totalAmountInFen).divide(BigDecimal.valueOf(100)); // 订单总金额（元）
        
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest(); // 即将发送给支付宝的请求（电脑网站支付请求）
        alipayRequest.setReturnUrl(alipayReturnUrl); // 浏览器端完成支付后跳转回商户的地址（同步通知）
        alipayRequest.setNotifyUrl(alipayNotifyUrl); // 支付宝服务端确认支付成功后通知商户的地址（异步通知）
        
        Map<String, Object> bizContent = new HashMap<>(); // biz - business
        // 填充业务参数
        bizContent.put("out_trade_no", "" + id + "-" + new Date().getTime()); // 商户订单号，加时间戳是为了避免测试时订单号重复
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY"); // 产品码，固定
        bizContent.put("total_amount", totalAmount); // 订单总金额（元）
        bizContent.put("subject", "shop手机商城订单支付"); // 订单标题
        bizContent.put("body", "TODO 显示订单项概要"); // 订单描述
        
        // 直接将完整的表单html输出到页面
        try {
            String bizContentStr = objectMapper.writeValueAsString(bizContent);
            logger.debug("alipay.bizContentStr: " + bizContentStr);
            alipayRequest.setBizContent(bizContentStr);
            String form = alipayClient.pageExecute(alipayRequest).getBody(); // 调用SDK生成支付表单
            orderMapper.setTotalAmount(id, totalAmountInFen);
            return form;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } 
    }

    @Override
    public void verifySignature(Map<String, String> paramMap)
            throws AlipaySignatureException {
        try {
            if (!AlipaySignature.rsaCheckV1(paramMap, alipayPublicKey, "UTF-8", alipaySignType)) {
                logger.warn("支付宝签名错误");
                throw new AlipaySignatureException();
            }
        } catch (AlipayApiException e) {
            throw new AlipaySignatureException(e);
        }
    }

    @Override
    public void handlePayResult(Map<String, String> paramMap) {
        // 验签
        verifySignature(paramMap);
        
        // 获取必要的请求参数
        String orderNumber = paramMap.get("out_trade_no");
        Long orderId = Long.valueOf(orderNumber.split("-")[0]);
        Integer totalAmount = new BigDecimal(paramMap.get("total_amount")).multiply(BigDecimal.valueOf(100)).intValue();
        String appId = paramMap.get("app_id");
        String tradeStatus = paramMap.get("trade_status");
        
        // 各种核对
        Order order = orderMapper.findOneToPay(orderId);
        if (order.getState() != OrderState.Created) {
            throw new AlipayResultException("订单状态非Created");
        }
        if (!order.getTotalAmount().equals(totalAmount)) {
            throw new AlipayResultException("订单总金额不一致");
        }
        if (!appId.equals(this.appId)) {
            throw new AlipayResultException("appId不一致");
        }
        
        if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
            // 设置订单状态为已支付
            orderMapper.setStateToPaid(orderId);
        }
    }

    @Scheduled(cron = "0 0 3 * * *") // 每天凌晨3点执行该方法
    @Override
    public void deleteCanceledOrders() {
        logger.info("删除过期已取消订单");
        Integer count = orderMapper.deleteCanceledOrders();
        logger.info("已删除" + count + "个订单");
    }

}
