package com.vti.demo.repository;

import com.vti.demo.entity.Order;
import com.vti.demo.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(OrderStatus status);

    List<Order> findByStatus(String status);

    List<Order> findByCustomerId(Long customerId);



}