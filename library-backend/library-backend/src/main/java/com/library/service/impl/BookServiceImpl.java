package com.library.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.library.entity.Book;
import com.library.mapper.BookMapper;
import com.library.service.BookService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {
    // 必须重写BookService中定义的listByTitle方法
    @Override
    public List<Book> listByTitle(String title) {
        QueryWrapper<Book> wrapper = new QueryWrapper<>();
        if (title != null) {
            wrapper.like("title", title);
        }
        return this.list(wrapper);
    }
}