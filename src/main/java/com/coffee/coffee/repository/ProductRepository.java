package com.coffee.coffee.repository;

import com.coffee.coffee.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    // 상품의 아이디를 역순으로 정렬하여 상품 목록을 보여줘야함
    List<Product> findProductByOrderByIdDesc();

    // image 컬럼에 특정 문자열이 포함된 데이터를 조회합니다.
    // 데이터 베이스의 like 키워드와 유사함
    // select * from products where image like'%big%'
    List<Product> findByImageContaining(String keyword);

    // 검색 조건인 spec와 페이징 객체 pageable을 사용하여 데이터를 검색
    // 정렬 방식은 pageable 객체에 포함되어 있음.
    Page<Product> findAll(Specification<Product> spec, Pageable pageable);
}
