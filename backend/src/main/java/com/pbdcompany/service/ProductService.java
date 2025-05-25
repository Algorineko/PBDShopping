
package com.pbdcompany.service;


import com.pbdcompany.dto.response.ProductResponse;
import com.pbdcompany.entity.Product;
import com.pbdcompany.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;

    // 查询所有商品
    public List<Product> findAll() {
        return productMapper.findAll();
    }

    // 根据ID删除商品
    public void deleteById(int id) {
        productMapper.deleteById(id);
    }

    // 添加商品
    public int insert(Product product) {
        return productMapper.insert(product);
    }

    // 更新商品信息
    public void update(Product product) {
        productMapper.update(product);
    }

    // 根据ID查询商品信息
    public Product findById(int id) {
        return productMapper.findById(id);
    }

    // 根据Id或名称查询商品信息
    public List<ProductResponse> findByNameOrId(String name, int id) {
        //修改目标：分别通过名称和id查询商品信息。然后将所有查询结果封装成List<ProductResponse>返回
        //productMapper.现在有查询商品名称的接口findByName(String name)，以及查询商品id的接口findById(int id)

            List<Product> products = new ArrayList<>();

            if (name != null && !name.isEmpty()) {
                products.addAll(productMapper.findByName(name));
            }

            if (id > 0) {
                Product product = productMapper.findById(id);
                if (product != null) {
                    products.add(product);
                }
            }

            // 去重处理（避免同时传name和id导致重复）
            return products.stream()
                    .distinct()
                    .map(p -> new ProductResponse(p.getProductId(), p.getProductName(), p.getPrice()))
                    .collect(Collectors.toList());
        }

}
