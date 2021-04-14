package com.bjtu.bookshop.service;

import com.bjtu.bookshop.entity.User;

public interface UserService {
    public void add(User user);
    public void update(User user);
    public void del(Integer id);
    public User findById(Integer id);
    public User findByEmail(String email);
    public User findByName(String name);
}
