package com.coffee.coffee.service;

import com.coffee.coffee.entity.Order;
import com.coffee.coffee.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository ;

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    public List<Order> findByMemberId(Long memberId) {
        // 쿼리 메소드를 사용하여 특정 회원의 주문 날짜가 최신인 것부터 조회합니다.
        // cf. 좀 더 복잡한 쿼리를 사용하시려면 @Query 또는 Querydsl을 사용
        return orderRepository.findByMemberIdOrderByIdDesc(memberId);
    }

    public List<Order> findAllOrders() {
        return orderRepository.findAllByOrderByIdDesc();
    }
}
