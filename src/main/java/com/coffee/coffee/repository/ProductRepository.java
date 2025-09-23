package com.coffee.coffee.repository;

import com.coffee.coffee.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    // 상품의 아이디를 역순으로 정렬하여 상품 목록을 보여줘야함
    List<Product> findProductByOrderByIdDesc();
}
