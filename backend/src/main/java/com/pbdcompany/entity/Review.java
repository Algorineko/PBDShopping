package com.pbdcompany.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 客户评价
 */
@Entity
@Data
@Table
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long customerId;
    private Long orderId;
    private Integer rating; // 1-5
    private String comment;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String images; // base64 encoded image data（JSON）
    private LocalDateTime createAt = LocalDateTime.now();
}
