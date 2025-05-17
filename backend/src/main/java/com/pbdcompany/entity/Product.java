package com.pbdcompany.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Product实体类
 */
@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;

    public Product(Long id, String name, Double price){
        this.id = id;
        this.name = name;
        this.price = price;
    }
    private Integer stock;
    private String categoryId; //类别编号
}
