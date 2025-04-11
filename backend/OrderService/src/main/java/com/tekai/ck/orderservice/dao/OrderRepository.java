/*
 * Copyright (c) 2025 Tek-AI LLC
 * All rights reserved.
 *
 * Created on 11-Apr-2025.
 */

package com.tekai.ck.orderservice.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import com.tekai.ck.orderservice.model.Order;
import com.tekai.ck.orderservice.model.OrderStatus;

@Component
public interface OrderRepository extends JpaRepository<Order, Long>
{
    @EntityGraph(attributePaths = "orderItems")
    List<Order> findAll();

    // Find orders by customer ID
    List<Order> findByCustomerId(Long customerId);

    // Find orders by status
    List<Order> findByStatus(OrderStatus status);

    // Find orders created between dates
    List<Order> findByOrderDateBetween(LocalDateTime start, LocalDateTime end);

    // Find orders with total amount greater than specified value
    List<Order> findByTotalAmountGreaterThan(double amount);

    // Custom query to find orders with specific items
    @Query("SELECT o FROM Order o JOIN o.orderItems i WHERE i.productId = :productId")
    List<Order> findOrdersContainingProduct(Long productId);

}