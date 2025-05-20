
package com.pbdcompany.service;

import org.example.springbootmybatis.entity.ReturnExchangeRequest;
import org.example.springbootmybatis.mapper.ReturnExchangeRequestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReturnExchangeRequestService {

    @Autowired
    private ReturnExchangeRequestMapper returnExchangeRequestMapper;

    // 查询所有退换货请求
    public List<ReturnExchangeRequest> findAll() {
        return returnExchangeRequestMapper.findAll();
    }

    // 根据ID删除退换货请求
    public void deleteById(int id) {
        returnExchangeRequestMapper.deleteById(id);
    }

    // 添加退换货请求
    public void insert(ReturnExchangeRequest returnExchangeRequest) {
        returnExchangeRequestMapper.insert(returnExchangeRequest);
    }

    // 更新退换货请求信息
    public void update(ReturnExchangeRequest returnExchangeRequest) {
        returnExchangeRequestMapper.update(returnExchangeRequest);
    }

    // 根据ID查询退换货请求信息
    public ReturnExchangeRequest findById(int id) {
        return returnExchangeRequestMapper.findById(id);
    }
}