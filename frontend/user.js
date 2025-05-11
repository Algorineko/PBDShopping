// user.js
import request from "@/utils/http";

// 注册账号
export const registerAPI = (data) => {
  return request({
    url: "/api/customer/register",
    method: "POST",
    data,
  });
};

// 查找商品
export const searchProductAPI = (params) => {
  return request({
    url: "/api/customer/product/search",
    method: "GET",
    params,
  });
};

// 加入购物车
export const addCartAPI = (data) => {
  return request({
    url: "/api/customer/cart/add",
    method: "POST",
    data,
  });
};

// 浏览购物车
export const getCartAPI = () => {
  return request({
    url: "/api/customer/cart",
    method: "GET",
  });
};

// 提交订单
export const createOrderAPI = () => {
  return request({
    url: "/api/customer/order/create",
    method: "POST",
  });
};

// 付款
export const payOrderAPI = (data) => {
  return request({
    url: "/api/customer/order/pay",
    method: "POST",
    data,
  });
};

// 物流跟踪
export const trackOrderAPI = (orderId) => {
  return request({
    url: `/api/customer/order/${orderId}/tracking`,
    method: "GET",
  });
};

// 确认收货
export const confirmReceiptAPI = (orderId) => {
  return request({
    url: `/api/customer/order/${orderId}/confirm-receipt`,
    method: "POST",
  });
};

// 顾客评价
export const submitReviewAPI = (orderId, data) => {
  return request({
    url: `/api/customer/order/${orderId}/review`,
    method: "POST",
    data,
  });
};

// 退货换货
export const returnOrderAPI = (orderId) => {
  return request({
    url: `/api/customer/order/${orderId}/return`,
    method: "POST",
  });
};
