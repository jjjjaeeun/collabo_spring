package com.coffee.coffee.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 장바구니에 담을 상품에 대한 정보를 가지고 있는 엔터티 클래스
@Getter
@Setter
@ToString
@Entity
@Table(name ="cart_products")
public class CartProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cart_products_id")
    private Long id ; // 카트 상품의 아이디(primary key)

    // cart 1개에는 여러개의 'cartproduct'를 담을 수 있음
    // JoinColumn에 명시한 cart_id는 외래 키 입니다
    // 이 컬럼은 primary key의 이름을 그대로 복사해서 사용하면 됨
    // mappedBy 구문이 없는 곳이 '연관 관계'의 주인이 되면, 외래키를 관리해주는 주체
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    // 동일 품목의 상품은 여러개의 cartproduct에 담겨질수 있습니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity ; // 구매 수량
}
