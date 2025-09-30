package com.coffee.coffee.repository;

import com.coffee.coffee.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
