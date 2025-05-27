package com.pbdcompany.mapper;

import com.pbdcompany.entity.Merchant;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;


import com.pbdcompany.entity.Merchant;
import org.apache.ibatis.annotations.*;

@Mapper
public interface MerchantMapper {

    List<Merchant> findAll();

    void deleteById(int id);

    void insert(Merchant merchant);

    void update(Merchant merchant);

    Merchant findById(int id);


    Merchant findByMerchantName(String merchantName);  // 查询店铺名是否存在

    Merchant findByUsername(String username);

    Merchant findByPhoneNumber(String phoneNumber);    // 查询手机号是否存在
}


