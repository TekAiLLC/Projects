/*
 * Copyright (c) 2025 Tek-AI LLC
 * All rights reserved.
 *
 * Created on 11-Apr-2025.
 */

package com.tekai.ck.orderservice.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id")
    private Long customerId;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(length = 500)
    private String deliveryAddress;

    @Column(length = 255)
    private String notes;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<OrderItem> orderItems = new ArrayList<>();

// Helper method to add order items
    public void addOrderItem(final OrderItem item)
    {
        orderItems.add(item);
        item.setOrder(this);
        // recalculateTotalAmount();
    }

// Helper method to remove order items
    public void removeOrderItem(final OrderItem item)
    {
        orderItems.remove(item);
        item.setOrder(null);
        // recalculateTotalAmount();
    }

// Helper method to calculate total amount
    private void recalculateTotalAmount()
    {
        totalAmount = orderItems.stream().map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

// Enum for order status
    

    public void setOrderDate(final LocalDateTime now)
    {
        this.orderDate = now;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(final Long id)
    {
        this.id = id;
    }

    public Long getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(final Long customerId)
    {
        this.customerId = customerId;
    }

    public OrderStatus getStatus()
    {
        return status;
    }

    public void setStatus(final OrderStatus status)
    {
        this.status = status;
    }

    public BigDecimal getTotalAmount()
    {
        return totalAmount;
    }

    public void setTotalAmount()
    {
        recalculateTotalAmount();
    }

    public String getDeliveryAddress()
    {
        return deliveryAddress;
    }

    public void setDeliveryAddress(final String shippingAddress)
    {
        deliveryAddress = shippingAddress;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes(final String notes)
    {
        this.notes = notes;
    }

    public List<OrderItem> getOrderItems()
    {
        return orderItems;
    }

    public void setOrderItems(final List<OrderItem> orderItems)
    {
        this.orderItems = orderItems;
    }

    public LocalDateTime getOrderDate()
    {
        return orderDate;
    }

}
