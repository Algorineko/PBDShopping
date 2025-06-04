package com.pbdcompany.controller;

import com.pbdcompany.dto.request.AddProductRequest;
import com.pbdcompany.dto.request.UpdateProductRequest;
import com.pbdcompany.dto.response.ProductInfoResponse;
import com.pbdcompany.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/merchant/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 商家查询自己的商品
     */
    @GetMapping("/list")
    public List<ProductInfoResponse> getProductsByMerchant(@RequestParam int merchantId) {
        return productService.getProductsByMerchant(merchantId);
    }
    /**
     * 用户根据分类 ID 获取商品列表
     */
    @GetMapping("/category/{categoryId}")
    public List<ProductInfoResponse> getProductsByCategoryId(@PathVariable int categoryId) {
        return productService.getProductsByCategoryId(categoryId);
    }

    /**
     * 用户通过商品名称关键词搜索商品
     */
    @GetMapping("/search")
    public List<ProductInfoResponse> searchProductsByName(@RequestParam String keyword) {
        return productService.searchProductsByName(keyword);
    }

    /**
     * 商家上架商品 + 多图上传
     */
    @PostMapping("/add")
    public String addProduct(@RequestBody AddProductRequest request) {
        if (productService.addProduct(request)) {
            return "商品上架成功";
        } else {
            return "商品上架失败";
        }
    }

    /**
     * 商家修改商品信息
     */
    @PutMapping("/update")
    public String updateProduct(@RequestBody UpdateProductRequest request) {
        if (productService.updateProduct(request)) {
            return "商品信息更新成功";
        } else {
            return "商品不存在或无权限操作";
        }
    }

    /**
     * 商家删除商品
     */
    @DeleteMapping("/delete")
    public String deleteProduct(@RequestParam int productId) {
        if (productService.deleteProduct(productId)) {
            return "商品下架成功";
        } else {
            return "商品不存在或删除失败";
        }
    }

    /**
     * 根据商品 ID 获取商品详细信息
     */
    @GetMapping("/detail/{productId}")
    public ProductInfoResponse getProductById(@PathVariable int productId) {
        return productService.getProductById(productId);
    }


}
