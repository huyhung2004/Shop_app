package com.example.shop.app.services;

import com.example.shop.app.DTO.ProductDTO;
import com.example.shop.app.DTO.ProductImageDTO;
import com.example.shop.app.exception.DataNotFoundException;
import com.example.shop.app.exception.InvalidParamException;
import com.example.shop.app.models.Product;
import com.example.shop.app.models.ProductImage;
import com.example.shop.app.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

public interface IProductService {
    Product createProduct(ProductDTO productDTO) throws DataNotFoundException;
    Product getProductById(Long id) throws DataNotFoundException;
    Page<ProductResponse> getAllProducts(PageRequest pageRequest);
    Product updateProduct(Long id,ProductDTO productDTO) throws DataNotFoundException;
    void deleteProduct(Long id);
    boolean existsByName(String name);

    ProductImage createProductImage(long id, ProductImageDTO productImageDTO)
            throws DataNotFoundException, InvalidParamException;
}
