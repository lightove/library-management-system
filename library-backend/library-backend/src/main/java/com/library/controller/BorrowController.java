package com.library.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.library.entity.Book;
import com.library.entity.Borrow;
import com.library.service.BookService;
import com.library.service.BorrowService;
import com.library.vo.Result;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 必须加@RestController，确保接口映射
@RestController
@RequestMapping("/borrow")
public class BorrowController {
    @Resource
    private BorrowService borrowService;
    @Resource
    private BookService bookService;

    // 借阅图书接口（新增：确保borrowDate/dueDate有值）
    @PostMapping("/add")
    public Result<?> borrowBook(@RequestBody Borrow borrow) {
        if (borrow.getUserId() == null || borrow.getBookId() == null) {
            return Result.error("用户ID/图书ID不能为空");
        }
        // 检查图书库存
        Book book = bookService.getById(borrow.getBookId());
        if (book == null || book.getStock() <= 0) {
            return Result.error("图书无库存");
        }
        // 手动设置借阅日期和应还日期
        borrow.setBorrowDate(LocalDateTime.now());
        borrow.setDueDate(LocalDate.now().plusDays(30));
        borrow.setStatus(0); // 未归还
        borrow.setCreateTime(LocalDateTime.now());
        // 保存借阅记录
        borrowService.save(borrow);
        // 扣减库存
        book.setStock(book.getStock() - 1);
        bookService.updateById(book);
        return Result.success("借阅成功");
    }

    // 我的借阅记录（核心：返回borrowDate/dueDate）
    @GetMapping("/user/{userId}")
    public Result<?> getUserBorrows(@PathVariable Long userId) {
        if (userId == null || userId <= 0) {
            return Result.error("用户ID无效");
        }
        LambdaQueryWrapper<Borrow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Borrow::getUserId, userId);
        List<Borrow> borrows = borrowService.list(wrapper);
        
        // 手动拼接格式化的时间字段
        List<Map<String, Object>> result = new ArrayList<>();
        for (Borrow borrow : borrows) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", borrow.getId());
            map.put("bookId", borrow.getBookId());
            map.put("status", borrow.getStatus());
            // 格式化借阅日期
            map.put("borrowDateStr", borrow.getBorrowDate() != null ? 
                borrow.getBorrowDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "-");
            // 格式化应还日期
            map.put("dueDateStr", borrow.getDueDate() != null ? 
                borrow.getDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "-");
            result.add(map);
        }
        return Result.success(result);
    }

    // 归还图书接口
    @PostMapping("/return/{id}")
    public Result<?> returnBook(@PathVariable Long id) {
        Borrow borrow = borrowService.getById(id);
        if (borrow == null) {
            return Result.error("借阅记录不存在");
        }
        if (borrow.getStatus() == 1) {
            return Result.error("该图书已归还");
        }
        // 更新归还状态
        borrow.setStatus(1);
        borrow.setReturnDate(LocalDate.now());
        borrowService.updateById(borrow);
        // 恢复库存
        Book book = bookService.getById(borrow.getBookId());
        if (book != null) {
            book.setStock(book.getStock() + 1);
            bookService.updateById(book);
        }
        return Result.success("归还成功");
    }

    // 借阅统计接口
    @GetMapping("/stats")
    public Result<?> getBorrowStats() {
        LambdaQueryWrapper<Borrow> unReturnedWrapper = new LambdaQueryWrapper<>();
        unReturnedWrapper.eq(Borrow::getStatus, 0);
        long unReturned = borrowService.count(unReturnedWrapper);

        LambdaQueryWrapper<Borrow> returnedWrapper = new LambdaQueryWrapper<>();
        returnedWrapper.eq(Borrow::getStatus, 1);
        long returned = borrowService.count(returnedWrapper);

        // 返回标准Map，确保前端能解析
        Map<String, Long> statsMap = new HashMap<>();
        statsMap.put("unReturned", unReturned);
        statsMap.put("returned", returned);
        return Result.success(statsMap);
    }
}