package com.coffee.coffee.repository;

import com.coffee.coffee.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    // 상품의 아이디를 역순으로 정렬하여 상품 목록을 보여줘야함
    List<Product> findProductByOrderByIdDesc();

    // image 컬럼에 특정 문자열이 포함된 데이터를 조회합니다.
    // 데이터 베이스의 like 키워드와 유사함
    // select * from products where image like'%big%'
    List<Product> findByImageContaining(String keyword);
}
