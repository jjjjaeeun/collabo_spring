package com.coffee.coffee.repository;

import com.coffee.coffee.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByMemberIdOrderByIdDesc(Long memberId);

    // 주문번호(id) 기준으로 모든 주문 내역을 역순(내림차순)으로 조회하려면 JPA 메서드를 이렇게 작성
    List<Order> findAllByOrderByIdDesc();
}
