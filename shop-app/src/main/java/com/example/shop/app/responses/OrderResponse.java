package com.example.shop.app.responses;

import com.example.shop.app.models.Order;
import com.example.shop.app.models.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor //hàm khởi tạo có tham số
@NoArgsConstructor //hàm khởi tạo ko tham số
@Builder
public class OrderResponse extends BaseResponse{
    private Long id;
    @JsonProperty("user_id")
    private long userID;
    private String fullname;
    private String email;
    @JsonProperty("phone_number")
    private String phoneNumber;
    private String address;
    private String note;
    private String status;
    @JsonProperty("order_date")
    private Date orderDate;
    @JsonProperty("total_money")
    private Float totalMoney;
    @JsonProperty("shipping_method")
    private String shippingMethod;
    @JsonProperty("shipping_address")
    private String shippingAddress;
    @JsonProperty("payment_method")
    private String paymentMethod;
}
