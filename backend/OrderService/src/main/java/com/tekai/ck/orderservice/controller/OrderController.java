/*
 * Copyright (c) 2025 Tek-AI LLC
 * All rights reserved.
 *
 * Created on 11-Apr-2025.
 */

package com.tekai.ck.orderservice.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tekai.ck.orderservice.exceptions.IllegalStatusArgument;
import com.tekai.ck.orderservice.model.Order;
import com.tekai.ck.orderservice.model.OrderStatus;
import com.tekai.ck.orderservice.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController
{

    private final OrderService orderService;

    public OrderController(final OrderService orderService)
    {
        this.orderService = orderService;
    }

    // Create a new order
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody final Order order)
    {
        final Order createdOrder = orderService.createOrder(order);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    // Get all orders
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders()
    {
        final List<Order> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // Get order by ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable final Long id)
    {
        final Order order = orderService.getOrderById(id);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    // Update an order
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable final Long id, @RequestBody final Order orderDetails)
    {
        final Order updatedOrder = orderService.updateOrder(id, orderDetails);
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }

    // Update order status
    @PatchMapping("/{id}")
    public ResponseEntity<Order> updateOrderDeliveryAddress(@PathVariable final Long id,
            @RequestBody final Map<String, String> addressDetails)
    {
        // supported field updates (deliveryAddress, status)
        
        final String newAddress = addressDetails.getOrDefault("deliveryAddress", "");
        Order updatedOrder = orderService.updateOrderAddress(id, newAddress);
        
        if (addressDetails.containsKey("status"))
        {
            final String newStatus = addressDetails.get("status");
            try
            {
                updatedOrder = orderService.updateOrderStatus(id, Enum.valueOf(OrderStatus.class, newStatus));
            }
            catch (IllegalArgumentException ex)
            {
                throw new IllegalStatusArgument(newStatus + " is not valid as Order status value, supported values are " + Arrays.toString(OrderStatus.values()));
            }
        }
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }

    // Delete an order
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable final Long id)
    {
        orderService.deleteOrder(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Get orders by customer ID
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Order>> getOrdersByCustomerId(@PathVariable final Long customerId)
    {
        final List<Order> orders = orderService.getOrdersByCustomerId(customerId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // Get orders by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable final OrderStatus status)
    {
        final List<Order> orders = orderService.getOrdersByStatus(status);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // Get orders containing a specific product
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Order>> getOrdersContainingProduct(@PathVariable final Long productId)
    {
        final List<Order> orders = orderService.getOrdersContainingProduct(productId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
