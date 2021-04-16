package com.bjtu.bookshop.controller;

import com.bjtu.bookshop.entity.Book;
import com.bjtu.bookshop.service.BookService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.UUID;


@Controller
@RequestMapping(value = "/book")
public class BookContentController {
    @Resource
    private BookService bookService;

    //每页字节数
    @Value("512")
    private int PAGE_BYTES;
    //总页数
    private int pages;
    //总内容
    private byte[] content;
    //总字节数
    private int bytes;
    //总字数
    private int num;
    private String contents;

    private static final Logger LOG = LoggerFactory.getLogger(BookContentController.class);

    @GetMapping(value = "/read")
    public String getBook(@RequestParam(value = "bookId") int bookId, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Book requestBook = bookService.findById(bookId);
        model.addAttribute("requestBook", requestBook);
        String bookContent;
        try {
            setContent(bookId);
            bookContent = getContent(pageNum);
            model.addAttribute("bookContent", bookContent);
            model.addAttribute("totalPage", getPages());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return "content";
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
            String relativePath = "\\src\\main\\resources\\books\\" + year + "\\" + month + "\\" + date;
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
            book.setFilepath(relativePath + "\\" + fileName);
            bookService.add(book);
            session.setAttribute("msg", "文件上传成功！");
        }
        return "redirect:/index";
    }

    @GetMapping(value = "/del")
    public String delBook(@RequestParam(value = "bookId") int bookId, HttpServletRequest request, Model model) {
        Book existing = bookService.findById(bookId);
        if (existing == null) {
            model.addAttribute("msg", "书本不存在!");
            return "index";
        }

        //定义文件路径
        String filePath = existing.getFilepath();
        //这里因为我文件是相对路径 所以需要在路径前面加一个点
        File file = new File("." + filePath);
        if (file.exists()) {//文件是否存在
            file.delete();//删除文件
        }

        bookService.del(bookId);
        model.addAttribute("msg", "删除成功！");

        return "redirect:/index";
    }

    @GetMapping(value = "/download")
    public ResponseEntity<byte[]> downloadBook(@RequestParam(value = "bookId") int bookId, @RequestHeader("User-Agent") String userAgent, HttpServletRequest request, Model model) throws Exception {
        Book book = bookService.findById(bookId);
        String localPath = new File("").getAbsolutePath();
        String relativePath = book.getFilepath();
        String filename = relativePath.substring(relativePath.lastIndexOf("\\") + 1);
        // 构建File
        File file = new File(localPath + relativePath);
        // ok表示Http协议中的状态 200
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        // 内容长度
        builder.contentLength(file.length());
        // application/octet-stream ： 二进制流数据（最常见的文件下载）
        builder.contentType(MediaType.APPLICATION_OCTET_STREAM);
        // 使用URLDecoder.decode对文件名进行解码
        filename = URLEncoder.encode(filename, "UTF-8");
        // 设置实际的响应文件名，告诉浏览器文件要用于【下载】、【保存】attachment 以附件形式
        // 不同的浏览器，处理方式不同，要根据浏览器版本进行区别判断
        if (userAgent.indexOf("MSIE") > 0) {
            // 如果是IE，只需要用UTF-8字符集进行URL编码即可
            builder.header("Content-Disposition", "attachment; filename=" + filename);
        } else {
            // 而FireFox、Chrome等浏览器，则需要说明编码的字符集
            // 注意filename后面有个*号，在UTF-8后面有两个单引号！
            builder.header("Content-Disposition", "attachment; filename*=UTF-8''" + filename);
        }

        return builder.body(FileUtils.readFileToByteArray(file));
    }

    //根据书籍id设置文件
    public void setContent(Integer id) throws UnsupportedEncodingException {
        Book book = bookService.findById(id);
        String localPath = new File("").getAbsolutePath();
        String relativePath = book.getFilepath();
        readTxtFile(localPath + relativePath);
    }

    //读取文件得到总内容与总字节
    public void readTxtFile(String filePath) throws UnsupportedEncodingException {
        File textFile = new File(filePath);
        bytes = (int) textFile.length();
        System.out.println(bytes);
        content = new byte[(int) textFile.length()];
        try (FileInputStream fileInputStream = new FileInputStream(textFile)) {
            fileInputStream.read(content);
            System.out.println(new String(content, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        contents = new String(content, "UTF-8");
    }

    //得到总页数
    public int getPages() {
        num = contents.length();
        pages = num / PAGE_BYTES + 1;
        return pages;
    }

    //根据页数返回内容
    public String getContent(int page) throws UnsupportedEncodingException {
        String contents = new String(content, "UTF-8");
        String contxt = "";
        if (page == pages) {
            contxt = contents.substring((page - 1) * PAGE_BYTES, num);
        } else {
            contxt = contents.substring((page - 1) * PAGE_BYTES, page * PAGE_BYTES);
        }
        return contxt;
    }
}
