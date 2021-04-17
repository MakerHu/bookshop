package com.bjtu.bookshop.controller;


import com.bjtu.bookshop.entity.User;
import com.bjtu.bookshop.service.UserService;
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

    @Resource
    private UserService userService;


    private static final byte[] DES_KEY = { 21, 1, -110, 82, -32, -85, -128, -65 };
    /**
     * 加密方法
     * @param pwd
     * @return
     */
    public static String encodeStr(String pwd)
    {
        Base64 base64 = new Base64();
        byte[] enbytes = base64.encodeBase64Chunked(pwd.getBytes());
        return new String(enbytes);
    }

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

        String encryptPassword = encodeStr(user.getPassword());
        user.setPassword(encryptPassword);
        user.setRole("customer");
        userService.add(user);
        model.addAttribute("msg","注册成功！");

        return "login";
    }

}
