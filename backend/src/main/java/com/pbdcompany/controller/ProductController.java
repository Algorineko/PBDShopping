package com.pbdcompany.controller;


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

    // 商家查看自己的商品
    @GetMapping("/list")
    public List<ProductInfoResponse> getProductsByMerchant(@RequestParam int merchantId) {
        return productService.getProductsByMerchant(merchantId);
    }

    // 商家修改商品信息
    @PutMapping("/update")
    public String updateProduct(@RequestBody UpdateProductRequest request) {
        if (productService.updateProduct(request)) {
            return "商品信息更新成功";
        } else {
            return "商品不存在或无权限操作";
        }
    }
}
