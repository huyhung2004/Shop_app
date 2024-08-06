package com.example.shop.app.services;

import com.example.shop.app.DTO.OrderDTO;
import com.example.shop.app.DTO.OrderDetailDTO;
import com.example.shop.app.exception.DataNotFoundException;
import com.example.shop.app.models.Order;
import com.example.shop.app.models.OrderDetail;

import java.util.List;

public interface IOrderDetailService {
    OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws DataNotFoundException;
    OrderDetail getOrderDetail(Long id) throws DataNotFoundException;

    OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException;
    void deleteOrderDetail(Long id);
    List<OrderDetail> findOrderId(Long orderId);
}
