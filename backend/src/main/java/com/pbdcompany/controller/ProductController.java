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
            @RequestParam(required = false) int id) {
                return productService.findByNameOrId(name, id);
    }

}
