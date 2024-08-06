package com.example.shop.app.services;

import com.example.shop.app.DTO.OrderDetailDTO;
import com.example.shop.app.exception.DataNotFoundException;
import com.example.shop.app.models.Order;
import com.example.shop.app.models.OrderDetail;
import com.example.shop.app.models.Product;
import com.example.shop.app.repositories.OrderDetailRepository;
import com.example.shop.app.repositories.OrderRepository;
import com.example.shop.app.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService{
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
        Order order=orderRepository.findById( orderDetailDTO.getOrderId())
                .orElseThrow(()->new DataNotFoundException("Can't find order by id: "+orderDetailDTO.getOrderId()));
        Product product=productRepository.findById(orderDetailDTO.getProductId()).
                orElseThrow(()->new DataNotFoundException("Can't find user by id: "+orderDetailDTO.getProductId()));
        OrderDetail orderDetail=OrderDetail.builder()
                .color(orderDetailDTO.getColor())
                .price(orderDetailDTO.getPrice())
                .product(product)
                .order(order)
                .numberOfProduct(orderDetailDTO.getNumberOfProducts())
                .totalMoney(orderDetailDTO.getTotalMoney())
                .build();

        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public OrderDetail getOrderDetail(Long id) throws DataNotFoundException {
        return orderDetailRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException("Can't find order detail by id: "+id));
    }

    @Override
    public OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
        Order order=orderRepository.findById( orderDetailDTO.getOrderId())
                .orElseThrow(()->new DataNotFoundException("Can't find order by id: "+orderDetailDTO.getOrderId()));
        Product product=productRepository.findById(orderDetailDTO.getProductId()).
                orElseThrow(()->new DataNotFoundException("Can't find user by id: "+orderDetailDTO.getProductId()));
        OrderDetail orderDetail=orderDetailRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException("Can't find order detail by id: "+id));

        orderDetail.setOrder(order);
        orderDetail.setColor(orderDetailDTO.getColor());
        orderDetail.setPrice(orderDetailDTO.getPrice());
        orderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
        orderDetail.setProduct(product);
        orderDetail.setNumberOfProduct(orderDetailDTO.getNumberOfProducts());
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public void deleteOrderDetail(Long id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public List<OrderDetail> findOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }


}
