package com.bjtu.bookshop.controller;

import com.bjtu.bookshop.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

//@RestController
@Controller
public class BookShopController {

    @GetMapping(value = {"/", "/index"})
    public String getIndex(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        System.out.println(session.getAttribute("msg"));
        return "index";
    }
}
