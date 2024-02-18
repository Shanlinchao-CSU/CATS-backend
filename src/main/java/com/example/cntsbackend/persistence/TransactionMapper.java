package com.example.cntsbackend.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.cntsbackend.domain.Transaction;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionMapper extends BaseMapper<Transaction> {
}
