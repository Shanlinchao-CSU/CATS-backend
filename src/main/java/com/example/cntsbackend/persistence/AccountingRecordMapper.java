package com.example.cntsbackend.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.cntsbackend.domain.AccountingRecord;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountingRecordMapper extends BaseMapper<AccountingRecord> {
}
