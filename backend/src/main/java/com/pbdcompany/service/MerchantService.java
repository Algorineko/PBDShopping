package com.pbdcompany.service;

import com.pbdcompany.dto.request.MerchantRegisterRequest;
import com.pbdcompany.dto.response.MerchantResponse;
import com.pbdcompany.entity.Merchant;
import com.pbdcompany.mapper.MerchantMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MerchantService {

    @Autowired
    private MerchantMapper merchantMapper;

    // 注册商家
    public boolean register(MerchantRegisterRequest request) {
        // 检查店铺名或手机号是否已存在
        if (merchantMapper.findByMerchantName(request.getMerchantName()) != null ||
                merchantMapper.findByPhoneNumber(request.getPhoneNumber()) != null) {
            return false;
        }

        Merchant merchant = new Merchant();
        BeanUtils.copyProperties(merchant, request);
        merchant.setPassword(request.getPassword()); // 可选加密
        merchantMapper.insert(merchant);
        return true;
    }

    // 查询所有商家
    public List<MerchantResponse> getAllMerchants() {
        return merchantMapper.findAll().stream()
                .map(this::convertToResponse)
                .toList();
    }

    // 根据ID查询商家
    public MerchantResponse getMerchantById(int id) {
        Merchant merchant = merchantMapper.findById(id);
        return convertToResponse(merchant);
    }

    // 登录验证
    public MerchantResponse login(String merchantName, String password) {
        Merchant merchant = merchantMapper.findByMerchantName(merchantName);
        if (merchant != null && merchant.getPassword().equals(password)) {
            return convertToResponse(merchant);
        }
        return null;
    }

    // 转换 Entity -> Response
    private MerchantResponse convertToResponse(Merchant merchant) {
        if (merchant == null) return null;
        MerchantResponse response = new MerchantResponse();
        BeanUtils.copyProperties(response, merchant);
        return response;
    }
}
