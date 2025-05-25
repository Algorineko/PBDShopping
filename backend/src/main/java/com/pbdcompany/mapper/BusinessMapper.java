package com.pbdcompany.mapper;

import com.pbdcompany.entity.Business;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BusinessMapper {
    void insert(Business business);
    Business selectById(Long id);
    Business selectByUsername(String username);
}
