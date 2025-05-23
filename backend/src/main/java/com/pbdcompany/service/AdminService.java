
package com.pbdcompany.service;


import com.pbdcompany.entity.Admin;
import com.pbdcompany.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminMapper adminMapper;

    // 查询所有管理员信息
    public List<Admin> findAll() {
        return adminMapper.findAll();
    }

    // 根据ID删除管理员
    public void deleteById(int id) {
        adminMapper.deleteById(id);
    }

    // 添加管理员
    public void insert(Admin admin) {
        adminMapper.insert(admin);
    }

    // 更新管理员信息
    public void update(Admin admin) {
        adminMapper.update(admin);
    }

    // 根据ID查询管理员信息
    public Admin findById(int id) {
        return adminMapper.findById(id);
    }
}