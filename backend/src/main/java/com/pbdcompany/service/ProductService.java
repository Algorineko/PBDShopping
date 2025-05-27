package com.pbdcompany.service;

import com.pbdcompany.dto.request.AddProductRequest;
import com.pbdcompany.dto.request.UpdateProductRequest;
import com.pbdcompany.dto.response.ProductInfoResponse;
import com.pbdcompany.dto.response.ProductResponse;
import com.pbdcompany.entity.Product;
import com.pbdcompany.entity.ProductCategory;
import com.pbdcompany.entity.ProductImage;
import com.pbdcompany.mapper.ProductCategoryMapper;
import com.pbdcompany.mapper.ProductImageMapper;
import com.pbdcompany.mapper.ProductMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductImageMapper productImageMapper;

    @Autowired
    private ProductCategoryMapper  productCategoryMapper;
    // 查询所有商品
    public List<Product> findAll() {
        return productMapper.findAll();
    }

    // 获取所有商品类别
    public List<ProductCategory> findAllCategory() {
        return productCategoryMapper.findAll();

    }
    // 根据ID删除商品
    public void deleteById(int id) {
        productMapper.deleteById(id);
    }

    // 添加商品
    public void insert(Product product) {
        productMapper.insert(product);
    }

    // 更新商品信息
    public void update(Product product) {
        productMapper.update(product);
    }

    // 根据ID查询商品信息
    public Product findById(int id) {
        return productMapper.findById(id);
    }

    // 根据Id 查询商品图片
    public String getProductImage(int productId) {
       ProductImage productImage = productImageMapper.findByProductId(productId);
       return productImage.getImage();
    }
    // 根据名称或ID搜索商品（顾客使用）
    public List<ProductResponse> findByNameOrId(String name) {
        return productMapper.findByName(name);
    }

    // 查看商家自己的商品（新增）
    public List<ProductInfoResponse> getProductsByMerchant(int merchantId) {
        // 校验参数合法性
        if (merchantId <= 0) {
            throw new IllegalArgumentException("Invalid merchant ID");
        }

        // 查询数据库
        List<Product> products = productMapper.findByMerchantId(merchantId);

        // 日志记录查询结果
        if (products == null || products.isEmpty()) {
            System.out.println("【INFO】未找到该商家的商品数据，MerchantID: " + merchantId);
            return Collections.emptyList();
        }

        System.out.println("【DEBUG】查询到商品数量: " + products.size());

        // 转换为响应对象
        return products.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    // 修改商品信息（新增）
    public boolean updateProduct(UpdateProductRequest request) {
        Product product = productMapper.findById(request.getProductId());

        if (product == null || product.getMerchantId() != request.getMerchantId()) {
            return false; // 商品不存在或无权限
        }

        if (request.getProductName() != null) {
            product.setProductName(request.getProductName());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }

        productMapper.updateSelective(product);
        return true;
    }

    // 转换 Entity -> Response
    private ProductInfoResponse convertToResponse(Product product) {
        if (product == null) return null;
        ProductInfoResponse response = new ProductInfoResponse();
        BeanUtils.copyProperties(product, response);
        return response;
    }

    public boolean addProduct(AddProductRequest request) {
        // 调用 Mapper 插入新商品
        return productMapper.insertProduct(request) > 0;
    }

    public boolean deleteProduct(int productId) {
        return productMapper.deleteProductById(productId) > 0;
    }

}
