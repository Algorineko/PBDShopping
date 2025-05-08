package com.pbdcompany.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 客户评价
 */
@Entity
@Data
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String customerId;
    private String orderId;
    private Integer rating; // 1-5
    private String comment;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String images; // base64 encoded image data（JSON）
    private LocalDateTime createAt = LocalDateTime.now();
}
