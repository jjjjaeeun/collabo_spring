package com.coffee.coffee.service;

import com.coffee.coffee.entity.Product;
import com.coffee.coffee.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // 상품에 대한 여러가지 로직 정보를 처리해주는 서비스 클래스
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getProductList() {
        return this.productRepository.findProductByOrderByIdDesc();
    }
}
