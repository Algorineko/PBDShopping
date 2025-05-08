package com.pbdcompany.controller;


import com.pbdcompany.dto.response.ProductResponse;
import com.pbdcompany.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customer/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping("/search")
    public List<ProductResponse> searchProduct(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String id) {
            /*
                @RequestParam(required = false)：
                    表示该请求参数是可选的（非必需）
                    如果请求中没有传入该参数，其值将为 null
             */
                return productService.searchProduct(name, id);
    }

}
