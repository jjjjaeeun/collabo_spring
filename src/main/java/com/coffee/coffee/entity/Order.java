package com.coffee.coffee.entity;

import com.coffee.coffee.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "orders") // 주의) order은 데이터 베이스 전용 키워드임, 복수로 사용하기
public class Order { // 주문과 관련된 Entity
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id")
    private Long id;

    // 고객 1명이 여러개의 주문을 할 수 있음.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 통상적으로 우리가 주문을 할 때, 여러개의 주문상품을 동시에 주문함
    // 하나의 주문에는 주문상품을 여러개 담을 수 있습니다.
    // 주의) mappedBy 항목의 "order"는 OrderProduct에 들어있는 Oder 타입의 변수명
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProducts;

    private LocalDate orderdate; // 주문 날짜

    private OrderStatus status; // 주문 상태


}
