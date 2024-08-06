package com.example.shop.app.controller;

import com.example.shop.app.DTO.OrderDetailDTO;
import com.example.shop.app.exception.DataNotFoundException;
import com.example.shop.app.models.OrderDetail;
import com.example.shop.app.responses.OrderDetailResponse;
import com.example.shop.app.services.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/order_details")
@RequiredArgsConstructor
public class OrderDetailController {
    private final OrderDetailService orderDetailService;
    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO){
        try {
            OrderDetail newOrderDetail= orderDetailService.createOrderDetail(orderDetailDTO);
            return ResponseEntity.ok().body(OrderDetailResponse.fromOrderDetail(newOrderDetail));
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@Valid @PathVariable("id") long id) throws DataNotFoundException {
        return ResponseEntity.ok(orderDetailService.getOrderDetail(id));


    }
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("orderId") long orderId){
        List<OrderDetail> orderDetails=orderDetailService.findOrderId(orderId);
        List<OrderDetailResponse> orderDetailResponses=orderDetails.stream().map(OrderDetailResponse::fromOrderDetail).toList();
        return ResponseEntity.ok(orderDetailResponses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(@Valid @PathVariable("id") long id, @RequestBody OrderDetailDTO newOrderDetailData) throws DataNotFoundException {
        OrderDetail orderDetail= orderDetailService.updateOrderDetail(id,newOrderDetailData);
        return ResponseEntity.ok(orderDetail);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrderDetail(@Valid @PathVariable("id") long id){
        orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.ok("delete order detail successfully");
    }
}
