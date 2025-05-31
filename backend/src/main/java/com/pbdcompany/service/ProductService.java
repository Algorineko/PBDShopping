package com.pbdcompany.service;

import com.pbdcompany.dto.request.AddProductRequest;
import com.pbdcompany.dto.request.UpdateProductRequest;
import com.pbdcompany.dto.response.ProductInfoResponse;
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
    private ProductCategoryMapper productCategoryMapper;

    @Autowired
    private ProductImageMapper productImageMapper;

    public List<ProductInfoResponse> getProductsByMerchant(int merchantId) {
        List<Product> products = productMapper.findByMerchantId(merchantId);
        if (products == null || products.isEmpty()) return Collections.emptyList();

        System.out.println("【DEBUG】查询到的商品：" + products);

        return products.stream().map(product -> {
            ProductInfoResponse response = new ProductInfoResponse();
            BeanUtils.copyProperties(product,response);

            // 设置分类名称
            ProductCategory category = productCategoryMapper.findById(product.getCategoryId());
            if (category != null) {
                response.setCategoryName(category.getCategoryName());
            }

            // 设置图片列表
            response.setImages(productImageMapper.findImagesByProductId(product.getProductId()));

            return response;
        }).collect(Collectors.toList());
    }

    /**
     * 用户根据分类 ID 获取所有商品
     */
    public List<ProductInfoResponse> getProductsByCategoryId(int categoryId) {
        List<Product> products = productMapper.findByCategoryId(categoryId);
        if (products == null || products.isEmpty()) return Collections.emptyList();

        return products.stream().map(product -> {
            ProductInfoResponse response = new ProductInfoResponse();
            BeanUtils.copyProperties( product,response);

            // 设置分类名称
            ProductCategory category = productCategoryMapper.findById(categoryId);
            if (category != null) {
                response.setCategoryName(category.getCategoryName());
            }

            // 设置图片列表
            response.setImages(productImageMapper.findImagesByProductId(product.getProductId()));

            return response;
        }).collect(Collectors.toList());
    }

    /**
     * 用户通过商品名称关键词搜索商品
     */
    public List<ProductInfoResponse> searchProductsByName(String keyword) {
        List<Product> products = productMapper.findByNameLike(keyword);
        if (products == null || products.isEmpty()) return Collections.emptyList();

        return products.stream().map(product -> {
            ProductInfoResponse response = new ProductInfoResponse();
            BeanUtils.copyProperties (product,response);

            // 设置分类名称
            ProductCategory category = productCategoryMapper.findById(product.getCategoryId());
            if (category != null) {
                response.setCategoryName(category.getCategoryName());
            }

            // 设置图片列表
            response.setImages(productImageMapper.findImagesByProductId(product.getProductId()));

            return response;
        }).collect(Collectors.toList());
    }

    /**
     * 商家上架商品并上传多张图片
     */
    public boolean addProduct(AddProductRequest request) {
        // 1. 插入商品主信息
        Product product = new Product();
        product.setCategoryId(request.getCategoryId());
        product.setMerchantId(request.getMerchantId());
        product.setProductName(request.getProductName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());

        if (productMapper.insert(product) <= 0) {
            return false; // 插入失败
        }

        // 2. 插入图片信息
        int productId = product.getProductId(); // 获取自动生成的 ID
        for (String image : request.getImages()) {
            ProductImage productImage = new ProductImage();
            productImage.setProductId(productId);
            productImage.setImage(image);

            if (productImageMapper.insert(productImage) <= 0) {
                // 可选：回滚商品插入 or 忽略
                return false;
            }
        }

        return true;
    }


    /**
     * 商家修改商品信息
     */
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
        if (request.getCategoryId() > 0) {
            product.setCategoryId(request.getCategoryId());
        }

        return productMapper.update(product) > 0;
    }

    /**
     * 商家删除商品
     */
    public boolean deleteProduct(int productId) {
        return productMapper.deleteById(productId) > 0;
    }


}
