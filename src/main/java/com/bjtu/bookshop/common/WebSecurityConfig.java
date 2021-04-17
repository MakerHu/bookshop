package com.bjtu.bookshop.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //authorizeRequests:声明配置是权限配置
        //antMatchers：拦截路径
        //permitAll：任何权限都可以访问
        //anyRequest：任何请求
        //authenticated：认证后才能访问
        //and().csrf().disable()：固定写法，表示csrf拦截失效
        http
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable();
    }
}
