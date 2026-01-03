package com.library.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.library.entity.User;
import com.library.service.UserService;
import com.library.vo.Result;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 必须加@RestController，确保接口映射
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    // 登录接口
    @PostMapping("/login")
    public Result<?> login(@RequestBody User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername())
               .eq(User::getPassword, user.getPassword());
        User loginUser = userService.getOne(wrapper);
        if (loginUser == null) {
            return Result.error("用户名或密码错误");
        }
        return Result.success(loginUser);
    }

    // 注册接口（强制设置createTime）
    @PostMapping("/register")
    public Result<?> register(@RequestBody User user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return Result.error("用户名不能为空");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return Result.error("密码不能为空");
        }
        // 检查用户名重复
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername().trim());
        if (userService.count(wrapper) > 0) {
            return Result.error("用户名已存在");
        }
        // 手动设置注册时间，确保数据库有值
        user.setCreateTime(LocalDateTime.now());
        user.setRole(1); // 普通用户
        user.setStatus(0); // 待审核
        userService.save(user);
        return Result.success("注册成功，请等待审核");
    }

    // 待审核用户列表（核心：返回createTime字段）
    @GetMapping("/wait-audit")
    public Result<?> getWaitAuditUsers() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getStatus, 0).eq(User::getRole, 1);
        List<User> users = userService.list(wrapper);
        
        // 手动拼接格式化的注册时间
        List<Map<String, Object>> result = new ArrayList<>();
        for (User user : users) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", user.getId());
            map.put("username", user.getUsername());
            // 直接返回格式化后的字符串
            map.put("createTimeStr", user.getCreateTime() != null ? 
                user.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "-");
            result.add(map);
        }
        return Result.success(result);
    }

    // 用户审核接口
    @PostMapping("/audit/{id}/{status}")
    public Result<?> auditUser(@PathVariable Long id, @PathVariable Integer status) {
        User user = userService.getById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        user.setStatus(status);
        userService.updateById(user);
        return Result.success(status == 1 ? "审核通过" : "审核拒绝");
    }
}