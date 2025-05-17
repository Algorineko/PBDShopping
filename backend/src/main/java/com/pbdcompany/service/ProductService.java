package com.pbdcompany.service;

import com.pbdcompany.dto.response.ProductResponse;
import com.pbdcompany.entity.Product;
import com.pbdcompany.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<ProductResponse> searchProduct(String name, Long id){
        List<Product> products = productRepository.findByNameContainingOrId(name,id);
        return products.stream()
                .map(product -> new ProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getStock()
                ) ).toList();
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
