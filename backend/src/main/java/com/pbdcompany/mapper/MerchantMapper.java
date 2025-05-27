package com.pbdcompany.mapper;

import com.pbdcompany.dto.response.MerchantProfileResponse;
import com.pbdcompany.entity.Merchant;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface MerchantMapper {

    List<Merchant> findAll();

    void deleteById(int id);

    void insert(Merchant merchant);

    void update(Merchant merchant);

    Merchant findById(int id);


    Merchant findByUsername(String username);

    Merchant findByUsernameAndPassword(String username, String password);

    MerchantProfileResponse findProfileByUsername(String username);
}


