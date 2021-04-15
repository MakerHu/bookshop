package com.bjtu.bookshop.service;

import com.bjtu.bookshop.entity.Book;

import java.util.List;

public interface BookService {
    public void add(Book book);

    public void update(Book book);

    public void del(Integer id);

    public Book findById(Integer id);

    public Book findByName(String name);

    public List<Book> findAll(int currentPageNum,int rowPerPage);

    public int count();
}
