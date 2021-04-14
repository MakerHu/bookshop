package com.bjtu.bookshop.controller;

import com.bjtu.bookshop.common.Result;
import com.bjtu.bookshop.entity.User;
import com.bjtu.bookshop.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Resource
    private UserService userService;

    @PostMapping
    public Result<User> login(@RequestParam String email, @RequestParam String password){
        try{
            User user = userService.findByEmail(email);
            if (user != null){
                if (user.getPassword().equals(password)){
                    //消除返回前端的收能过户数据中的重要信息
                    user.setPassword("");
                    return Result.success(user);
                }else {
                    return Result.error("1", "用户名或密码错误！");
                }
            }else{
                return Result.error("2", "用户名或密码错误！");
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
