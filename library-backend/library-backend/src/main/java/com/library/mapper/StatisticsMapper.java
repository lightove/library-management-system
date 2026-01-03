package com.library.mapper;

import com.library.controller.StatisticsController;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StatisticsMapper {
    // 总借阅数
    @Select("SELECT COUNT(*) FROM borrow")
    Integer countTotalBorrows();

    // 未归还数
    @Select("SELECT COUNT(*) FROM borrow WHERE status = '未归还'")
    Integer countUnReturnedBorrows();

    // 图书总数
    @Select("SELECT COUNT(*) FROM book")
    Integer countTotalBooks();

    // 热门图书
    @Select("SELECT book.title AS bookTitle, book.author, COUNT(borrow.id) AS borrowCount " +
            "FROM borrow LEFT JOIN book ON borrow.book_id = book.id " +
            "GROUP BY book.id, book.title, book.author ORDER BY borrowCount DESC LIMIT 10")
    List<StatisticsController.HotBookVO> selectHotBooks();

    // 月度趋势
    @Select("SELECT DATE_FORMAT(borrow_date, '%Y-%m') AS month, COUNT(*) AS count " +
            "FROM borrow WHERE borrow_date >= DATE_SUB(NOW(), INTERVAL 12 MONTH) " +
            "GROUP BY DATE_FORMAT(borrow_date, '%Y-%m') ORDER BY month ASC")
    List<StatisticsController.MonthTrendVO> selectMonthTrend();
}