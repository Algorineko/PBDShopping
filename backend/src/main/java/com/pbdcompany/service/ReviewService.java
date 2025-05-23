
package com.pbdcompany.service;

import com.pbdcompany.entity.Review;
import com.pbdcompany.mapper.ReviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewMapper reviewMapper;

    // 查询所有评论
    public List<Review> findAll() {
        return reviewMapper.findAll();
    }

    // 根据ID删除评论
    public void deleteById(int id) {
        reviewMapper.deleteById(id);
    }

    // 添加评论
    public void insert(Review review) {
        reviewMapper.insert(review);
    }

    // 更新评论信息
    public void update(Review review) {
        reviewMapper.update(review);
    }

    // 根据ID查询评论信息
    public Review findById(int id) {
        return reviewMapper.findById(id);
    }
}