package com.example.cntsbackend.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.cntsbackend.domain.Account;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountMapper extends BaseMapper<Account> {
}
