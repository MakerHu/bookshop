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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.SecureRandom;

@Controller
//@RequestMapping("/login")
public class LoginController {
    @Resource
    private UserService userService;


    private static final byte[] DES_KEY = { 21, 1, -110, 82, -32, -85, -128, -65 };


    /**
     * 解密方法
     * @param cryptData
     * @return
     */
    @SuppressWarnings("restriction")
    public static String decryptBasedDes(String cryptData) {
        String decryptedData = null;
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            DESKeySpec deskey = new DESKeySpec(DES_KEY);
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(deskey);
            // 解密对象
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key, sr);
            // 把字符串进行解码，解码为为字节数组，并解密
            decryptedData = new String(cipher.doFinal(new sun.misc.BASE64Decoder().decodeBuffer(cryptData)));
        } catch (Exception e) {
            throw new RuntimeException("解密错误，错误信息：", e);
        }
        return decryptedData;
    }


    @GetMapping(value = "/login")
    public String getLogin(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("msg", "");
        return "login";
    }

    @PostMapping(value = "/login")
    public String login(@ModelAttribute("user") User msgUser, Model model, HttpServletRequest request) {
        try {
//            System.out.println();
            User user = userService.findByEmail(msgUser.getEmail());
            if (user != null) {
                if (decryptBasedDes(user.getPassword()).equals(msgUser.getPassword())) {
                    //消除返回前端的收能过户数据中的重要信息
                    user.setPassword("");
                    HttpSession session = request.getSession();
                    session.setAttribute("user", user);
                    session.setAttribute("msg", "登录成功");
                    session.setAttribute("state", 1);
                    session.setAttribute("role", user.getRole());
                    return "redirect:/index";
                } else {
                    model.addAttribute("msg", "用户名或密码错误！");
                    return "login";
                }
            } else {
                model.addAttribute("msg", "用户名或密码错误！");
                return "login";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
