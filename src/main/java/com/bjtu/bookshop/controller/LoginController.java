package com.bjtu.bookshop.controller;

import com.bjtu.bookshop.entity.User;
import com.bjtu.bookshop.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
//@RequestMapping("/login")
public class LoginController {
    @Resource
    private UserService userService;

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
                if (user.getPassword().equals(msgUser.getPassword())) {
                    //消除返回前端的收能过户数据中的重要信息
                    user.setPassword("");
                    HttpSession session = request.getSession();
                    session.setAttribute("user", user);
                    session.setAttribute("msg", "登录成功");
                    session.setAttribute("state", 1);
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
