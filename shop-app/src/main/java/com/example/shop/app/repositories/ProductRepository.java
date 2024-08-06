package com.example.shop.app.repositories;

import com.example.shop.app.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product,Long> {
    Boolean existsByName(String Name); //kiểm tra xem sản phẩm tồn tại ko
    Page<Product> findAll(Pageable pageable);//Phân trang

}
