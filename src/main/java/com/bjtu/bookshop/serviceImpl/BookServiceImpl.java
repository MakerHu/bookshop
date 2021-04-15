package com.bjtu.bookshop.serviceImpl;

import com.bjtu.bookshop.dao.BookDao;
import com.bjtu.bookshop.entity.Book;
import com.bjtu.bookshop.service.BookService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    @Resource
    private BookDao bookDao;

    @Override
    public void add(Book book) {
        bookDao.save(book);
    }

    @Override
    public void update(Book book) {
        bookDao.save(book);
    }

    @Override
    public void del(Integer id) {
        bookDao.deleteById(id);
    }

    @Override
    public Book findById(Integer id) {
        return bookDao.findById(id).orElse(null);
    }

    @Override
    public Book findByName(String name) {
        return bookDao.findByName(name);
    }

    @Override
    public List<Book> findAll(int pageNum, int rowPerPage) {
        List<Book> bookList = bookDao.findAll();
        int size = bookList.size();
        int fromidx = (pageNum - 1) * rowPerPage;
        if (fromidx > size) fromidx = 0;
        int toidx = fromidx + rowPerPage;
        if (toidx > size) toidx = size;
        return bookList.subList(fromidx, toidx);
    }

    @Override
    public int count() {
        return bookDao.findAll().size();
    }


}
