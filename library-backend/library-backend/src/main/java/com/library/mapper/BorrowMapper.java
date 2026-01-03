package com.library.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.library.entity.Borrow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BorrowMapper extends BaseMapper<Borrow> {
    // 根据用户ID查询借阅记录
    @Select("SELECT b.*, book.title, book.author FROM borrow b LEFT JOIN book ON b.book_id = book.id WHERE b.user_id = #{userId}")
    List<Borrow> selectByUserId(Long userId);
}