package com.library.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Data
@TableName("borrow") // 对应数据库表
public class Borrow {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long bookId;
    private LocalDateTime borrowDate; // 对应borrow_date
    private LocalDate dueDate;        // 对应due_date
    private LocalDate returnDate;     // 对应return_date
    private Integer status;           // 0=未归还，1=已归还
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}