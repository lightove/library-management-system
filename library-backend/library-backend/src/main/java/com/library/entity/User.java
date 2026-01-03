package com.library.entity; 

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String realName;
    private Integer role;  // 0-管理员，1-普通用户
    private Integer status; // 0-待审核，1-已启用，2-禁用
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}