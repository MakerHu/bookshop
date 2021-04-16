package com.bjtu.bookshop.controller;

import com.bjtu.bookshop.entity.Book;
import com.bjtu.bookshop.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;


@Controller
@RequestMapping(value = "/book")
public class BookContentController {
    @Resource
    private BookService bookService;

    @Value("E:/test/")
    private String FILE_PATH;

    private static final Logger LOG = LoggerFactory.getLogger(BookContentController.class);

    @GetMapping(value = "/read")
    public String getBook(@RequestParam int bookId, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
//        if(request.getAttribute("state")!= null && request.getAttribute("state").equals("1")){
        Book requestBook = bookService.findById(bookId);
        model.addAttribute("requestBook", requestBook);

        return "content";

//        }else{
//            return null;
//        }
    }

    @PostMapping(value = "/add")
    public String addBook(@RequestParam(value = "file") MultipartFile file, @RequestParam(value = "name") String bookName, @RequestParam(value = "description") String bookDesc, HttpServletRequest request, Model model) throws Exception {
        HttpSession session = request.getSession();
//        System.out.println(bookName + "\t" + bookDesc);
        if (file.isEmpty()) {
            session.setAttribute("msg", "文件上传失败！");
        } else {
            //文件名
            String fileName = file.getOriginalFilename();
            //后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            if (!suffixName.equals(".txt")) {
                session.setAttribute("msg", "文件类型错误！");
                return "redirect:/index";
            }
            // 新文件名
            fileName = UUID.randomUUID() + suffixName;
            //获取系统时间
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int date = calendar.get(Calendar.DATE);
//            System.out.println(year + "/" + month + "/" + date);
            //创建文件夹
            String localPath = new File("").getAbsolutePath();
            String relativePath ="\\src\\main\\resources\\books\\" + year + "\\" + month + "\\" + date;
            localPath = localPath + relativePath;
//            System.out.println(filePath);
            File tempFile = new File(localPath, fileName);
            //判断路径是否存在，不存在则创建一个
            if (!tempFile.getParentFile().exists()) {
                tempFile.getParentFile().mkdirs();
            }
            //转存文件
            try {
                file.transferTo(tempFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Book book = new Book();
            book.setName(bookName);
            book.setDescription(bookDesc);
            book.setFilepath(relativePath);
            bookService.add(book);
            session.setAttribute("msg", "文件上传成功！");
        }
        return "redirect:/index";
    }

}
