package com.library.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("book") // 对应数据库表
public class Book {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String isbn;       // 新增字段
    private String title;
    private String author;
    private String publisher;  // 新增字段
    private Integer stock;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}