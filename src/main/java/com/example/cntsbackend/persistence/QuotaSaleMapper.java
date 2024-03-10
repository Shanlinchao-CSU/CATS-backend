package com.example.cntsbackend.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.cntsbackend.domain.QuotaSale;
import com.example.cntsbackend.dto.QuotaSaleDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface QuotaSaleMapper extends BaseMapper<QuotaSale> {
    @Select("SELECT qs.quota, qs.seller_id, qs.unit_price, qs.month, a.account_name " +
            "FROM quota_sale qs " +
            "JOIN account a ON qs.seller_id = a.account_id " +
            "WHERE qs.seller_id = #{seller_id}")
    QuotaSaleDto getQuotaSaleWithAccount(@Param("seller_id") int seller_id);
}
