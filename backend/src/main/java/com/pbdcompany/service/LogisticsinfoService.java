
package com.pbdcompany.service;

import com.pbdcompany.entity.Logisticsinfo;
import com.pbdcompany.mapper.LogisticsinfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogisticsinfoService {

    @Autowired
    private LogisticsinfoMapper logisticsinfoMapper;

    // 查询所有物流信息
    public List<Logisticsinfo> findAll() {
        return logisticsinfoMapper.findAll();
    }

    // 根据ID删除物流信息
    public void deleteById(int id) {
        logisticsinfoMapper.deleteById(id);
    }

    // 添加物流信息
    public void insert(Logisticsinfo logisticsinfo) {
        logisticsinfoMapper.insert(logisticsinfo);
    }

    // 更新物流信息
    public void update(Logisticsinfo logisticsinfo) {
        logisticsinfoMapper.update(logisticsinfo);
    }

    // 根据ID查询物流信息
    public Logisticsinfo findById(int id) {
        return logisticsinfoMapper.findById(id);
    }
}