package com.library.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.library.entity.User;
import com.library.mapper.UserMapper;
import com.library.service.UserService;
import com.library.vo.Result;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public Result<?> login(String username, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = baseMapper.selectOne(wrapper);
        
        if (user == null) return Result.error("用户名不存在");
        if (!password.equals(user.getPassword())) return Result.error("密码错误");
        if (user.getStatus() != 1) return Result.error("账号未启用");
        
        User respUser = new User();
        respUser.setId(user.getId());
        respUser.setUsername(user.getUsername());
        respUser.setRole(user.getRole());
        return Result.success(respUser);
    }
}