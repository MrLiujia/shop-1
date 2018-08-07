package shop.controller;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import shop.model.Cellphone;
import shop.service.CellphoneService;
import shop.service.IpService;

@Controller
public class IndexController extends BaseController {
    private CellphoneService cellphoneService;
    
    private IpService ipService;
   
    @Autowired
    public IndexController(CellphoneService cellphoneService,
                           IpService ipService) {
        this.cellphoneService = cellphoneService;
        this.ipService = ipService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String index(Model model, 
                        @ModelAttribute Cellphone cellphone) {
        logger.debug("手机搜索条件: " + cellphone);
        List<Cellphone> cellphones = cellphoneService.search(cellphone);
        model.addAttribute("cellphones", cellphones);
        return "index";
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/ip", produces = "text/plain; charset=utf-8")
    @ResponseBody
    public String ipToProvince(@RequestParam String ip) {
        return ipService.ipToProvince(ip);
    }
}
