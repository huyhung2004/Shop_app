package com.example.shop.app.repositories;

import com.example.shop.app.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    //tìm các đơn hàng của 1 user nào đó
    List<Order> findByUserId(Long userId);
}
