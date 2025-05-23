package com.pbdcompany.dto.request;

import lombok.Data;

@Data
public class ReviewRequest {
    private Integer orderItemId;
    private Integer rating;
    private String comment;
    private String images; // 可选
}
