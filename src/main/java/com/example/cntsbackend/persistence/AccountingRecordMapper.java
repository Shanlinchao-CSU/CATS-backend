package com.example.cntsbackend.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.cntsbackend.domain.AccountingRecord;
import com.example.cntsbackend.dto.AccountingRecordDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountingRecordMapper extends BaseMapper<AccountingRecord> {
    @Select("SELECT ar.id, ar.enterprise_id, ar.month, ar.time, ar.variable_json, ar.result, ar.state, ar.conductor_id, " +
            "a.account_name, a.enterprise_type " +
            "FROM accounting_record ar " +
            "INNER JOIN account a ON ar.enterprise_id = a.account_id " +
            "WHERE ar.enterprise_id = #{enterprise_id}")
    AccountingRecordDTO getAccountingRecordsWithAccountName(@Param("enterprise_id") int enterprise_id);
}
