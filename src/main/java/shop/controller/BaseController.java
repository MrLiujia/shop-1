package shop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import shop.service.IpService;

public abstract class BaseController {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private IpService ipService;

    @ModelAttribute // springmvc在执行控制器类中的@RequestMapping方法之前会自动调用@ModelAttribute方法
    public void prepareCommonModel(HttpServletRequest request,
                                   HttpSession session,
                                   Model model) {
        logger.debug("准备页面的公共数据");
        
        String ip = request.getRemoteAddr(); // 获取请求的ip地址
        String province = ipService.ipToProvince(ip);
        
        model.addAttribute("userProvince", province);
    }
}
