package com.library.controller;

import com.library.entity.Book;
import com.library.entity.Borrow;
import com.library.service.BookService;
import com.library.service.BorrowService;
import com.library.vo.Result;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {
    @Resource
    private BookService bookService;
    @Resource
    private BorrowService borrowService;

    // 图书入库
    @PostMapping
    public Result<?> addBook(@RequestBody Book book) {
        book.setCreateTime(LocalDateTime.now());
        bookService.save(book);
        return Result.success("图书入库成功");
    }

    // 图书查询
    @GetMapping
    public Result<?> getBooks(@RequestParam(required = false) String title) {
        List<Book> books = bookService.listByTitle(title);
        return Result.success(books);
    }

    // 图书借阅
    @PostMapping("/borrow/{userId}/{bookId}")
    public Result<?> borrowBook(@PathVariable Long userId, @PathVariable Long bookId) {
        Book book = bookService.getById(bookId);
        if (book == null || book.getStock() <= 0) {
            return Result.error("图书不存在或库存不足");
        }
        book.setStock(book.getStock() - 1);
        bookService.updateById(book);

        Borrow borrow = new Borrow();
        borrow.setUserId(userId);
        borrow.setBookId(bookId);
        borrow.setBorrowDate(LocalDateTime.now());
        borrow.setDueDate(LocalDate.now().plusDays(30));
        borrow.setStatus(0);
        borrow.setCreateTime(LocalDateTime.now());
        borrowService.save(borrow);
        return Result.success("借阅成功");
    }

    // 编辑图书
    @PutMapping
    public Result<?> updateBook(@RequestBody Book book) {
        bookService.updateById(book);
        return Result.success("编辑成功");
    }

    // 删除图书
    @DeleteMapping("/{id}")
    public Result<?> deleteBook(@PathVariable Long id) {
        bookService.removeById(id);
        return Result.success("删除成功");
    }
}