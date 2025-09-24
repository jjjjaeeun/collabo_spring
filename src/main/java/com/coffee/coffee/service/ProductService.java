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

    public boolean deleteProduct(Long id) {
        // existsById(), deleteById() 메소드는 CrudRepository에 포함되어 있음
        if(productRepository.existsById(id)){ //해당 항목이 존재하면

            this.productRepository.deleteById(id);// 삭제하기

            return true; // true의 의미는 삭제를 성공했습니다.

        }else {// 존재하지 않으면
            return false;
        }
    }
}
