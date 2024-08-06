package com.example.shop.app.DTO;

import com.example.shop.app.models.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductImageDTO {
    @JsonProperty("product_id")
    @Min(value = 1,message = "product's id must be > 0")
    private Long productId;

    @JsonProperty("image_url")
    @Size(min = 6,max = 200,message = "Image's name")
    private String imageUrl;
}
