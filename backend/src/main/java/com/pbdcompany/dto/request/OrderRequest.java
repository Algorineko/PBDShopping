package com.pbdcompany.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private int customerId;
    private double totalPrice;
    private List<OrderItemRequest> orderItems;

    @Data
    public static class OrderItemRequest {
        private int productId;
        private int quantity;
        private double price;
        private String selectedOptions; // 如颜色、尺寸，默认可为空
    }
}
