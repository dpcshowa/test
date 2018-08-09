package com.show.controller;

import com.show.pojo.PageMsg;
import com.show.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/item")
public class JdController {

    @Autowired
    private ItemService itemService;


    @RequestMapping("list")
    public String list(String queryString, String catalog_name, String price,
                             @RequestParam(required = false,defaultValue = "1") int page,
                             @RequestParam(defaultValue = "1") int sort,
                             @RequestParam(defaultValue = "30") int rows,
                             Model mv){

        PageMsg pageMsg = itemService.queryIndex(queryString,catalog_name,price,page,sort,rows);


        mv.addAttribute("queryString",queryString);
        mv.addAttribute("catalog_name",catalog_name);
        mv.addAttribute("price",price);
        mv.addAttribute("sort",sort);
        mv.addAttribute("result",pageMsg);

        return "product_list";
    }
}
