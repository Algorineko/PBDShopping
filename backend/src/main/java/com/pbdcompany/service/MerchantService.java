
package com.pbdcompany.service;


import com.pbdcompany.entity.Merchant;
import com.pbdcompany.mapper.MerchantMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MerchantService {

    @Autowired
    private MerchantMapper merchantMapper;

    // 查询所有商户信息
    public List<Merchant> findAll() {
        return merchantMapper.findAll();
    }

    // 根据ID删除商户
    public void deleteById(int id) {
        merchantMapper.deleteById(id);
    }

    // 添加商户
    public int insert(Merchant merchant) {
       return   merchantMapper.insert(merchant);
    }

    // 更新商户信息
    public void update(Merchant merchant) {
        merchantMapper.update(merchant);
    }

    // 根据ID查询商户信息
    public Merchant findById(int id) {
        return merchantMapper.findById(id);
    }
}