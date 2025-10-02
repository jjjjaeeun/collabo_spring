package com.coffee.coffee.repository;

import com.coffee.coffee.constant.OrderStatus;
import com.coffee.coffee.entity.Order;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // 쿼리 메소드를 사용하여 특정 회원의 주문 날짜가 최신인 것부터 조회합니다.
    // 주문의 상태가 PENDING인것만 조회합니다.
    // cf. 좀 더 복잡한 쿼리를 사용하시려면 @Query 또는 Querydsl을 사용
    List<Order> findByMemberIdAndStatusOrderByIdDesc(Long memberId, OrderStatus status);

    // 주문번호(id) 기준으로 모든 주문 내역을 역순(내림차순)으로 조회하려면 JPA 메서드를 이렇게 작성
    // 주문의 상태가 PENDING인것만 조회합니다.
    List<Order> findByStatusOrderByIdDesc(OrderStatus status);

    // 특정 주문에 대하여 주문의 상태를 주문완료로 변경함
    // 쿼리 메소드 대신 @Query 어노테이션 사용 예시: sql 대신 JPQL
    // 대소문자 구분하기, 테이블이름 x 엔터티이름 적기
    @Query("update Order o set o.status = :status where o.id = :orderId")
    @Modifying // 이 쿼리는 select 구문이 아니고 데이터 변경을 위한 쿼리임
    @Transactional
    int updateOrderStatus(@Param("orderId") Long orderId, @Param("status") OrderStatus status);

}
