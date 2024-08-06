package com.example.shop.app.controller;

import com.example.shop.app.DTO.OrderDTO;
import com.example.shop.app.DTO.UserDTO;
import com.example.shop.app.models.Order;
import com.example.shop.app.responses.OrderResponse;
import com.example.shop.app.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @PostMapping("")
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderDTO orderDTO, BindingResult result){
        try {
            if (result.hasErrors()){
                List<String> errorMessages= result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Order order= orderService.createOrder(orderDTO);
            return ResponseEntity.ok(order);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/user/{user_id}")
//    http://localhost:8088/api/v1/orders/user/3
    public ResponseEntity<?> getOrders(@Valid @PathVariable("user_id") long userId){
        try {
            List<Order> orders=orderService.findByUserId(userId);
            return ResponseEntity.ok(orders);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
//    http://localhost:8088/api/v1/orders/3
    public ResponseEntity<?> getOrderById(@Valid @PathVariable("id") long id){
        try {
            Order existingorder=orderService.getOrder(id);
            return ResponseEntity.ok(existingorder);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
//    http://localhost:8088/api/v1/orders/3
    public ResponseEntity<?> updateOrder(@Valid @PathVariable long id,@Valid @RequestBody OrderDTO orderDTO){
        try {
            Order order= orderService.updateOrder(id,orderDTO);
            return ResponseEntity.ok(order);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
//    http://localhost:8088/api/v1/orders/3
    public ResponseEntity<String> deleteOrder(@Valid @PathVariable long id){
        //xóa mềm - cập nhật active = false
        orderService.deleteOrder(id);
        return ResponseEntity.ok("order delete successfully");
    }

}
