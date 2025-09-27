package com.coffee.coffee.service;

import com.coffee.coffee.entity.CartProduct;
import com.coffee.coffee.repository.CartProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartProductService {
    private final CartProductRepository cartProductRepository;

    public void saveCartProduct(CartProduct cp) {
        this.cartProductRepository.save(cp);
    }
}
