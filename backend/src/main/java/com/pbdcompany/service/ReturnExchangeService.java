
package com.pbdcompany.service;


import com.pbdcompany.dto.request.ReturnExchangeRequest;
import com.pbdcompany.entity.ReturnExchange;
import com.pbdcompany.mapper.ReturnExchangeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReturnExchangeService {

    @Autowired
    private ReturnExchangeMapper returnExchangeMapper;

    // 查询所有退换货请求
    public List<ReturnExchange> findAll() {
        return returnExchangeMapper.findAll();
    }

    // 根据ID删除退换货请求
    public void deleteById(int id) {
        returnExchangeMapper.deleteById(id);
    }

    // 添加退换货请求
    public int insert(ReturnExchange returnExchange) {
        return returnExchangeMapper.insert(returnExchange);
    }

    // 更新退换货请求信息
    public void update(ReturnExchange returnExchange) {
        returnExchangeMapper.update(returnExchange);
    }

    // 根据ID查询退换货请求信息
    public ReturnExchange findById(int id) {
        return returnExchangeMapper.findById(id);
    }


    //  通过请求对象插入退换货的实体信息
    public ReturnExchange getByRequest(ReturnExchangeRequest request)
    {
        ReturnExchange returnExchange = new ReturnExchange();

        returnExchange.setOrderItemId(request.getOrderItemId());
        returnExchange.setRequestType(request.getRequestType());
        returnExchange.setReason(request.getReason());
        returnExchange.setLogisticsCompany(request.getLogisticsCompany());
        returnExchange.setTrackingNumber(request.getTrackingNumber());

        return returnExchange;
    }

}