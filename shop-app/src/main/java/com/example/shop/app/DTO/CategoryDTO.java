package com.example.shop.app.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

//Data Transfer Object
@Data
@Getter
@Setter
@AllArgsConstructor //hàm khởi tạo có tham số
@NoArgsConstructor //hàm khởi tạo ko tham số
public class CategoryDTO {
    @NotEmpty(message = "Category's name cannot be empty")
    private String name;


}
