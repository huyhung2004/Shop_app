package com.example.shop.app.services;

import com.example.shop.app.DTO.OrderDTO;
import com.example.shop.app.exception.DataNotFoundException;
import com.example.shop.app.models.Order;

import java.util.List;

public interface IOrderService {
    Order createOrder(OrderDTO orderDTO) throws DataNotFoundException;
    Order getOrder(Long id);

    Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException;
    void deleteOrder(Long id);
    List<Order> findByUserId(Long userId);
}
