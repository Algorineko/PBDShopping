// merchant.js
import request from "@/utils/http";

// 商家注册
export const merchantRegisterAPI = (data) => {
  return request({
    url: "/api/merchant/register",
    method: "POST",
    data,
  });
};

// 更新商品信息
export const updateProductAPI = (productId, data) => {
  return request({
    url: `/api/merchant/product/${productId}`,
    method: "PUT",
    data,
  });
};

// 处理订单（发货/退货/换货）
export const processOrderAPI = (orderId, data) => {
  return request({
    url: `/api/merchant/order/${orderId}/process`,
    method: "PUT",
    data,
  });
};

// 查询物流信息（商家）
export const getMerchantTrackingAPI = (orderId) => {
  return request({
    url: `/api/merchant/order/${orderId}/tracking`,
    method: "GET",
  });
};

// 提交物流信息
export const submitTrackingAPI = (orderId, data) => {
  return request({
    url: `/api/merchant/order/${orderId}/tracking`,
    method: "POST",
    data,
  });
};
