package com.bjtu.bookshop.common;

import com.bjtu.bookshop.entity.Book;
import com.bjtu.bookshop.service.BookService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class BookTool {
    //总页数
    private int pages;
    //总内容
    private byte[] content;
    //总字节数
    private int bytes;
    //总字数
    private int num;
    //每页字节数
    static private int pageBytes = 8;
    private String contents;
    private BookService bookService;
    public void setContent(Integer id) throws UnsupportedEncodingException {
        Book BookInfo = bookService.findById(id);
        String filepath = BookInfo.getFilepath();
        readTxtFile(filepath);
    }

    //读取文件得到总内容与总字节
    public void readTxtFile(String filePath) throws UnsupportedEncodingException {
        File textFile = new File(filePath);
        bytes = (int)textFile.length();
        System.out.println(bytes);
        content = new byte[(int)textFile.length()];
        try(FileInputStream fileInputStream = new FileInputStream(textFile) ){
            fileInputStream.read(content);
            System.out.println(new String(content,"UTF-8"));
        }catch (IOException e){
            e.printStackTrace();
        }
        contents = new String(content,"UTF-8");
    }
    //得到总页数
    public int getPages() {
        num = contents.length();
        pages = num/pageBytes + 1;
        return pages;
    }
    //根据页数返回内容
    public String getContent(int page) throws UnsupportedEncodingException {
        String contents = new String(content,"UTF-8");
        String contxt = "";
        if(page == pages) {
            contxt = contents.substring((page - 1) * pageBytes, num);
        }
        else{
            contxt = contents.substring((page-1)*pageBytes,page*pageBytes);
        }
        return contxt;
    }
}
