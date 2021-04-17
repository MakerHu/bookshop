package com.bjtu.bookshop.controller;


import com.bjtu.bookshop.entity.User;
import com.bjtu.bookshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;
import org.apache.commons.codec.binary.Base64;

@Controller
public class RegisterController {

    @Autowired
    private BCryptPasswordEncoder encoding;

    @Resource
    private UserService userService;


    @GetMapping(value = "/register")
    public String register(Model model){
        model.addAttribute("user",new User());
        return "register";
    }

    @PostMapping(value = "/register")
    public String registerUserAccount(@ModelAttribute("user") User user, Model model){
        User existing = userService.findByEmail(user.getEmail());
        if (existing != null){
            model.addAttribute("msg","邮箱已存在!");
            return "register";
        }

        // 密码加密
        String encryptPassword = encoding.encode(user.getPassword());
        user.setPassword(encryptPassword);
        user.setRole("customer");
        userService.add(user);
        model.addAttribute("msg","注册成功！");

        return "login";
    }

}
