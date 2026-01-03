package com.library.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.library.entity.Borrow;
import java.util.List;

public interface BorrowService extends IService<Borrow> {
    List<Borrow> listByUserId(Long userId);
}