package com.library.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.library.entity.User;
import com.library.vo.Result;

public interface UserService extends IService<User> {
    Result<?> login(String username, String password);
}