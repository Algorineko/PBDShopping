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
            @RequestParam(required = false) Integer id) {

        //5.26修改: 如果 id 为 null，传 0 或其他默认值给底层方法
        return productService.findByNameOrId(name, id != null ? id : 0);
    }
    //5.26修改：将int id改为Integer id，为了能使id赋值为null。

}
