package com.bjtu.bookshop.controller;

import com.bjtu.bookshop.entity.Book;
import com.bjtu.bookshop.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/book")
public class BookContentController {
    @Resource
    private BookService bookService;

    @GetMapping(value = "/read")
    public String getBook(@RequestParam int bookId, Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
//        if(request.getAttribute("state")!= null && request.getAttribute("state").equals("1")){
            Book requestBook = bookService.findById(bookId);
            model.addAttribute("requestBook",requestBook);

            return "content";

//        }else{
//            return null;
//        }
    }
}
