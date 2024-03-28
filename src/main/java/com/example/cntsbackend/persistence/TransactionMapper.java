package com.example.cntsbackend.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.cntsbackend.domain.Transaction;
import com.example.cntsbackend.dto.TransactionDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionMapper extends BaseMapper<Transaction> {
    @Select("SELECT t.transaction_id, t.amount, t.cost, t.complete_time, " +
            "a1.account_name AS sale_account_name, a2.account_name AS buyer_account_name " +
            "FROM transaction t " +
            "JOIN account a1 ON t.sale_id = a1.account_id " +
            "JOIN account a2 ON t.buyer_id = a2.account_id " +
            "WHERE t.sale_id = #{sale_id} AND t.buyer_id = #{buyer_id}")
    TransactionDto getTransactionWithAccountNames(@Param("sale_id") int sale_id, @Param("buyer_id") int buyer_id);

}
