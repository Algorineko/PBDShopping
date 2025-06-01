package com.pbdcompany.service;

import com.pbdcompany.dto.request.RegisterRequest;
import com.pbdcompany.dto.request.UpdateMerchantProfileRequest;
import com.pbdcompany.dto.response.MerchantProfileResponse;
import com.pbdcompany.dto.response.RegisterResponse;
import com.pbdcompany.entity.Merchant;
import org.springframework.stereotype.Service;

@Service
public interface MerchantService {
    /**
     * 注册商户
     */
    RegisterResponse register(RegisterRequest request);

    /**
     * 商户登录
     */
    Merchant login(String username, String password);

    /**
     * 根据用户名获取商户资料
     */
    MerchantProfileResponse getMerchantProfileByUsername(String username);

    /**
     * 更新商户资料
     */
    boolean updateMerchantProfile(String username, UpdateMerchantProfileRequest request);

    /**
     * 检查用户名是否已存在
     */
    boolean existsByUsername(String username);

    boolean changePassword(String username, String oldPassword, String newPassword);

}
