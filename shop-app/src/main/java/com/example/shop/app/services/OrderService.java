package com.example.shop.app.services;

import com.example.shop.app.DTO.OrderDTO;
import com.example.shop.app.exception.DataNotFoundException;
import com.example.shop.app.models.Order;
import com.example.shop.app.models.OrderStatus;
import com.example.shop.app.models.User;
import com.example.shop.app.repositories.OrderRepository;
import com.example.shop.app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    @Override
    public Order createOrder(OrderDTO orderDTO) throws DataNotFoundException {
        User existingUser =userRepository.findById(orderDTO.getUserID()).
                orElseThrow(()->new DataNotFoundException("Can't find user with id: "+orderDTO.getUserID()));
        //convert orderDTO -> order bằng model mapper
        modelMapper.typeMap(OrderDTO.class,Order.class).addMappings(mapper -> mapper.skip(Order::setId));
        Order order=new Order();
        modelMapper.map(orderDTO,order);
        order.setUser(existingUser);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);
        LocalDate shippingDate=orderDTO.getShippingDate()==null? LocalDate.now():orderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())){
            throw new DataNotFoundException("Data must be at least today");
        }
        order.setActive(true);
        order.setShippingDate(shippingDate);
        orderRepository.save(order);
        return order;
    }

    @Override
    public Order getOrder(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException {
        Order existingOrder =orderRepository.findById(id).
                orElseThrow(()->new DataNotFoundException("Can't find order with id: "+id));
        User existingUser =userRepository.findById(orderDTO.getUserID()).
                orElseThrow(()->new DataNotFoundException("Can't find user with id: "+orderDTO.getUserID()));
        modelMapper.typeMap(OrderDTO.class,Order.class).addMappings(mapper -> mapper.skip(Order::setId));
        modelMapper.map(orderDTO,existingOrder);
        existingOrder.setUser(existingUser);
        return orderRepository.save(existingOrder);
    }

    @Override
    public void deleteOrder(Long id) {
        Order order=orderRepository.findById(id).orElse(null);
        if (order!= null){
            order.setActive(false);
            orderRepository.save(order);
        }
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
