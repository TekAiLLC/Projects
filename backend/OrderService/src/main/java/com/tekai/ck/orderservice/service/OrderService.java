/*
 * Copyright (c) 2025 Tek-AI LLC
 * All rights reserved.
 *
 * Created on 11-Apr-2025.
 */

package com.tekai.ck.orderservice.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tekai.ck.orderservice.dao.OrderRepository;
import com.tekai.ck.orderservice.exceptions.ResourceNotFoundException;
import com.tekai.ck.orderservice.model.Order;
import com.tekai.ck.orderservice.model.OrderItem;
import com.tekai.ck.orderservice.model.OrderStatus;

import jakarta.transaction.Transactional;

@Service
public class OrderService
{

    private final OrderRepository orderRepository;

    public OrderService(final OrderRepository orderRepository)
    {
        this.orderRepository = orderRepository;
    }

    // Create a new order
    @Transactional
    public Order createOrder(final Order order)
    {
        order.setOrderDate(LocalDateTime.now());
        if (order.getStatus() == null)
        {
            order.setStatus(OrderStatus.CREATED);
        }
        final List<OrderItem> items = order.getOrderItems();
        for (final OrderItem item : items)
        {
            item.setOrder(order);
        }
        order.setOrderItems(items);
        
        order.setTotalAmount();
        return orderRepository.save(order);
    }

    // Get all orders
    public List<Order> getAllOrders()
    {
        final List<Order> orders = orderRepository.findAll();
        orders.forEach(o -> o.getOrderItems().size()); // triggers fetch
        return orders;
    }

    // Get order by ID
    public Order getOrderById(final Long id)
    {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    // Update an existing order
    @Transactional
    public Order updateOrder(final Long id, final Order orderDetails)
    {
        final Order order = getOrderById(id);

        order.setCustomerId(orderDetails.getCustomerId());
        order.setStatus(orderDetails.getStatus());
        order.setDeliveryAddress(orderDetails.getDeliveryAddress());
        order.setNotes(orderDetails.getNotes());

        // Handle order items if provided
        if (orderDetails.getOrderItems() != null && !orderDetails.getOrderItems().isEmpty())
        {
            order.getOrderItems().clear();
            orderDetails.getOrderItems().forEach(order::addOrderItem);
        }

        return orderRepository.save(order);
    }

    // Update order status
    @Transactional
    public Order updateOrderStatus(final Long id, final OrderStatus status)
    {
        final Order order = getOrderById(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }

    @Transactional
    public Order updateOrderAddress(final Long id, final String newAddress)
    {
        final Order order = getOrderById(id);
        if (!newAddress.equals(order.getDeliveryAddress()))
        {
            order.setDeliveryAddress(newAddress);
            return orderRepository.save(order);
        } else
        {
            return order;
        }
    }

    // Delete an order
    @Transactional
    public void deleteOrder(final Long id)
    {
        final Order order = getOrderById(id);
        orderRepository.delete(order);
    }

    // Find orders by customer ID
    public List<Order> getOrdersByCustomerId(final Long customerId)
    {
        return orderRepository.findByCustomerId(customerId);
    }

    // Find orders by status
    public List<Order> getOrdersByStatus(final OrderStatus status)
    {
        return orderRepository.findByStatus(status);
    }

    // Find orders within a date range
    public List<Order> getOrdersInDateRange(final LocalDateTime startDate, final LocalDateTime endDate)
    {
        return orderRepository.findByOrderDateBetween(startDate, endDate);
    }

    // Find orders containing a specific product
    public List<Order> getOrdersContainingProduct(final Long productId)
    {
        return orderRepository.findOrdersContainingProduct(productId);
    }
}