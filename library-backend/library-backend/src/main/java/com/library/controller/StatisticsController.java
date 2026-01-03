package com.library.controller;

import com.library.service.StatisticsService;
import com.library.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/core")
    public Result<CoreStatVO> getCoreStatistics() {
        try {
            CoreStatVO coreStat = statisticsService.getCoreStatistics();
            return Result.success(coreStat);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取核心统计数据失败：" + e.getMessage());
        }
    }

    @GetMapping("/hot-books")
    public Result<List<HotBookVO>> getHotBooks() {
        try {
            List<HotBookVO> hotBookList = statisticsService.getHotBooks();
            return Result.success(hotBookList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取热门图书排行失败：" + e.getMessage());
        }
    }

    @GetMapping("/month-trend")
    public Result<List<MonthTrendVO>> getMonthTrend() {
        try {
            List<MonthTrendVO> trendList = statisticsService.getMonthTrend();
            return Result.success(trendList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取月度借阅趋势失败：" + e.getMessage());
        }
    }

    // 静态内部VO类（MyBatis可识别）
    public static class CoreStatVO {
        private Integer totalBorrows;
        private Integer unReturnedCount;
        private Integer returnedCount;
        private Integer totalBooks;

        public Integer getTotalBorrows() { return totalBorrows; }
        public void setTotalBorrows(Integer totalBorrows) { this.totalBorrows = totalBorrows; }
        public Integer getUnReturnedCount() { return unReturnedCount; }
        public void setUnReturnedCount(Integer unReturnedCount) { this.unReturnedCount = unReturnedCount; }
        public Integer getReturnedCount() { return returnedCount; }
        public void setReturnedCount(Integer returnedCount) { this.returnedCount = returnedCount; }
        public Integer getTotalBooks() { return totalBooks; }
        public void setTotalBooks(Integer totalBooks) { this.totalBooks = totalBooks; }
    }

    public static class HotBookVO {
        private Integer rank;
        private String bookTitle;
        private String author;
        private Integer borrowCount;

        public Integer getRank() { return rank; }
        public void setRank(Integer rank) { this.rank = rank; }
        public String getBookTitle() { return bookTitle; }
        public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }
        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }
        public Integer getBorrowCount() { return borrowCount; }
        public void setBorrowCount(Integer borrowCount) { this.borrowCount = borrowCount; }
    }

    public static class MonthTrendVO {
        private String month;
        private Integer count;

        public String getMonth() { return month; }
        public void setMonth(String month) { this.month = month; }
        public Integer getCount() { return count; }
        public void setCount(Integer count) { this.count = count; }
    }
}