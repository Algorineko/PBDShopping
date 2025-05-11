// admin.js
import request from "@/utils/http";

// 审核商家商品
export const reviewProductAPI = (data) => {
  return request({
    url: "/api/admin/merchant/product/review",
    method: "POST",
    data,
  });
};

// 下架商品
export const unlistProductAPI = (productId, data) => {
  return request({
    url: `/api/admin/merchant/product/${productId}/unlist`,
    method: "PUT",
    data,
  });
};

// 关闭店铺
export const closeMerchantAPI = (merchantId, data) => {
  return request({
    url: `/api/admin/merchant/${merchantId}/close`,
    method: "PUT",
    data,
  });
};

// 分页查询顾客信息
export const getCustomersAPI = (params) => {
  return request({
    url: "/api/admin/customer",
    method: "GET",
    params,
  });
};

// 添加顾客
export const addCustomerAPI = (data) => {
  return request({
    url: "/api/admin/customer",
    method: "POST",
    data,
  });
};

// 更新顾客信息
export const updateCustomerAPI = (customerId, data) => {
  return request({
    url: `/api/admin/customer/${customerId}`,
    method: "PUT",
    data,
  });
};

// 删除顾客
export const deleteCustomerAPI = (customerId) => {
  return request({
    url: `/api/admin/customer/${customerId}`,
    method: "DELETE",
  });
};

// 分页查询分类
export const getCategoriesAPI = (params) => {
  return request({
    url: "/api/admin/category",
    method: "GET",
    params,
  });
};

// 添加分类
export const addCategoryAPI = (data) => {
  return request({
    url: "/api/admin/category",
    method: "POST",
    data,
  });
};

// 更新分类
export const updateCategoryAPI = (categoryId, data) => {
  return request({
    url: `/api/admin/category/${categoryId}`,
    method: "PUT",
    data,
  });
};

// 删除分类
export const deleteCategoryAPI = (categoryId) => {
  return request({
    url: `/api/admin/category/${categoryId}`,
    method: "DELETE",
  });
};

// 分页查询商品
export const getProductsAPI = (params) => {
  return request({
    url: "/api/admin/product",
    method: "GET",
    params,
  });
};

// 添加商品
export const addProductAPI = (data) => {
  return request({
    url: "/api/admin/product",
    method: "POST",
    data,
  });
};

// 更新商品
export const updateProductAdminAPI = (productId, data) => {
  return request({
    url: `/api/admin/product/${productId}`,
    method: "PUT",
    data,
  });
};

// 删除商品
export const deleteProductAdminAPI = (productId) => {
  return request({
    url: `/api/admin/product/${productId}`,
    method: "DELETE",
  });
};
