package com.library.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.library.entity.Book;
import java.util.List;

public interface BookService extends IService<Book> {
    List<Book> listByTitle(String title);
}