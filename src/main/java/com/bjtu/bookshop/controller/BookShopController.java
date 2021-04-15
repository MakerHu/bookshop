package com.bjtu.bookshop.controller;

import com.bjtu.bookshop.entity.Book;
import com.bjtu.bookshop.service.BookService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

//@RestController
@Controller
public class BookShopController {
    @Resource
    private BookService bookService;

    @Value("8")
    private int ROW_PER_PAGE;


    @RequestMapping(value = "/")
    public String root() {
        return "redirect:/index";
    }

    @GetMapping(value = "/index")
    public String getIndex(@RequestParam(value = "page", defaultValue = "1") int pageNum, Model model, HttpServletRequest request) {
//        HttpSession session = request.getSession();
//        System.out.println(session.getAttribute("msg"));
        List<Book> bookList = bookService.findAll(pageNum, ROW_PER_PAGE);

//        System.out.println(bookList.get(1).getName());

        int count = bookService.count();
        boolean hasPrev = pageNum > 1;
        boolean hasNext = (pageNum * ROW_PER_PAGE) < count;
        model.addAttribute("bookList", bookList);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("prev", pageNum - 1);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("next", pageNum + 1);
        return "index";
    }
}
