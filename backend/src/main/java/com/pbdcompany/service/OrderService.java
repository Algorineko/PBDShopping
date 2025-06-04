package com.pbdcompany.service;

import com.pbdcompany.dto.request.OrderCreateRequest;
import com.pbdcompany.dto.request.OrderItemRequest;
import com.pbdcompany.dto.request.OrderShippingRequest;
import com.pbdcompany.dto.response.OrderItemResponse;
import com.pbdcompany.dto.response.OrderResponse;
import com.pbdcompany.entity.LogisticsInfo;
import com.pbdcompany.entity.OrderItem;
import com.pbdcompany.entity.Orders;
import com.pbdcompany.entity.Product;
import com.pbdcompany.enums.Status;
import com.pbdcompany.mapper.LogisticsInfoMapper;
import com.pbdcompany.mapper.OrderItemMapper;
import com.pbdcompany.mapper.OrderMapper;
import com.pbdcompany.mapper.ProductMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private LogisticsInfoMapper logisticsInfoMapper;

    @Autowired
    private ProductMapper productMapper;

    // 获取用户的所有订单（含订单项）
    public List<OrderResponse> getOrdersByCustomerId(int customerId) {
        List<Orders> orders = orderMapper.findByCustomerId(customerId);
        return convertToResponses(orders);
    }

    // 获取商户的所有订单（含订单项）
    public List<OrderResponse> getOrdersByMerchantId(int merchantId) {
        List<Orders> orders = orderMapper.findByMerchantId(merchantId);
        return convertToResponses(orders);
    }

    // 转换实体类为响应对象
    private List<OrderResponse> convertToResponses(List<Orders> ordersList) {
        return ordersList.stream()
                .map(order -> {
                    OrderResponse response = new OrderResponse();
                    response.setOrderId(order.getOrderId());
                    response.setCustomerId(order.getCustomerId());
                    response.setMerchantId(order.getMerchantId());
                    response.setTotalPrice(order.getTotalPrice());
                    response.setStatus(order.getStatus().name());

                    // 查询订单项
                    List<OrderItem> items = orderItemMapper.findByOrderId(order.getOrderId());
                    System.out.println(items);
                    System.out.println(order.getOrderId());
                    List<OrderItemResponse> itemResponses = items.stream()
                            .map(item -> {
                                OrderItemResponse res = new OrderItemResponse();
                                res.setOrderItemId(item.getOrderItemId());
                                res.setProductId(item.getProductId());
                                res.setQuantity(item.getQuantity());
                                res.setPrice(item.getPrice());
                                return res;
                            })
                            .collect(Collectors.toList());

                    response.setItems(itemResponses);
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean shipOrder(OrderShippingRequest request) {
        List<LogisticsInfo> logisticsList = request.getLogisticsList();

        // 设置物流状态为 "SHIPPED"
        for (LogisticsInfo logistics : logisticsList) {
            logistics.setStatus(Status.SHIPPED);
        }

        // 批量插入物流信息
        logisticsInfoMapper.insertBatch(logisticsList);

        // 更新订单状态为 "SHIPPED"
        orderMapper.updateOrderStatus(request.getOrderId(), String.valueOf(Status.SHIPPED));

        return true;
    }

    @Transactional
    public boolean createOrder(OrderCreateRequest request) {
        try {
            List<OrderItemRequest> itemRequests = request.getItems();

            // 1. 批量获取所有商品的价格
            List<Integer> productIds = itemRequests.stream()
                    .map(OrderItemRequest::getProductId)
                    .distinct()
                    .toList();

            List<Product> products = new ArrayList<>();
            productIds.forEach(
                    productId -> {
                        Product product = productMapper.findById(productId);
                        products.add(product);
                    }
            );

            // 构建 productId -> price 映射
            Map<Integer, Double> priceMap = products.stream()
                    .collect(Collectors.toMap(Product::getProductId, Product::getPrice));

            // 2. 计算总价
            double totalPrice = itemRequests.stream()
                    .mapToDouble(item -> {
                        double price = priceMap.getOrDefault(item.getProductId(), 0.0);
                        return price * item.getQuantity();
                    })
                    .sum();

            // 3. 创建订单
            Orders order = new Orders();
            order.setCustomerId(request.getCustomerId());
            order.setMerchantId(request.getMerchantId());
            order.setTotalPrice(totalPrice);
            order.setStatus(Status.PENDING);
            orderMapper.insert(order);

            // 4. 创建订单项
            List<OrderItem> orderItems = itemRequests.stream()
                    .map(item -> {
                        OrderItem oi = new OrderItem();
                        oi.setOrderId(order.getOrderId());
                        oi.setProductId(item.getProductId());
                        oi.setQuantity(item.getQuantity());
                        oi.setPrice(priceMap.getOrDefault(item.getProductId(), 0.0));
                        return oi;
                    })
                    .collect(Collectors.toList());

            if (!orderItems.isEmpty()) {
                orderItemMapper.insertBatch(orderItems);
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 文件路径：/src/main/java/com/pbdcompany/service/OrderService.java

    public boolean updateOrderStatus(int itemId, String status) {
        try {
            orderMapper.updateOrderStatus(itemId, status);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
