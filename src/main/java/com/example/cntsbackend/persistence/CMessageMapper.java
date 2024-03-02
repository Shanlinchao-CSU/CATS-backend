package com.example.cntsbackend.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.cntsbackend.domain.CMessage;
import org.springframework.stereotype.Repository;

@Repository
public interface CMessageMapper extends BaseMapper<CMessage> {
}
