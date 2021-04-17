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

@Controller
public class RegisterController {

    @Resource
    private UserService userService;


    private static final byte[] DES_KEY = { 21, 1, -110, 82, -32, -85, -128, -65 };
    /**
     * 加密方法
     * @param data
     * @return
     */
    @SuppressWarnings("restriction")
    public static String encryptBasedDes(String data) {
        String encryptedData = null;
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            DESKeySpec deskey = new DESKeySpec(DES_KEY);
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(deskey);
            // 加密对象
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key, sr);
            // 加密，并把字节数组编码成字符串
            encryptedData = new sun.misc.BASE64Encoder().encode(cipher.doFinal(data.getBytes()));
        } catch (Exception e) {
            // log.error("加密错误，错误信息：", e);
            throw new RuntimeException("加密错误，错误信息：", e);
        }
        return encryptedData;
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

        String encryptPassword = encryptBasedDes(user.getPassword());
        user.setPassword(encryptPassword);
        user.setRole("customer");
        userService.add(user);
        model.addAttribute("msg","注册成功！");

        return "login";
    }

}
