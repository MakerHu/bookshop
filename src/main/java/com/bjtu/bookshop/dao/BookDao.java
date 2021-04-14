package com.bjtu.bookshop.dao;

import com.bjtu.bookshop.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookDao extends JpaRepository<Book, Integer> {
    Book findByName(String name);

    void deleteByName(String name);
}
