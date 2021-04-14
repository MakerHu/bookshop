package com.bjtu.bookshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
@Controller
@RequestMapping("/index")
public class BookShopController {

    @RequestMapping
    public String index(){
        return "index";
    }
}
