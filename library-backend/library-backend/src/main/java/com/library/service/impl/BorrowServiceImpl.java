package com.library.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.library.entity.Borrow;
import com.library.mapper.BorrowMapper;
import com.library.service.BorrowService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BorrowServiceImpl extends ServiceImpl<BorrowMapper, Borrow> implements BorrowService {
    @Override
    public List<Borrow> listByUserId(Long userId) {
        QueryWrapper<Borrow> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return list(wrapper);
    }
}