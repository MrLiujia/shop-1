package shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import shop.model.Cellphone;
import shop.service.CellphoneService;

@Controller
public class CellphoneController extends BaseController {
    private CellphoneService cellphoneService;

    @Autowired
    public CellphoneController(CellphoneService cellphoneService) {
        this.cellphoneService = cellphoneService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/cellphones/{id}")
    public String details(@PathVariable Long id,
                          Model model) {
        Cellphone cellphone = cellphoneService.findOne(id);
        model.addAttribute("cellphone", cellphone);
        return "cellphone-details";
    }
}
