package com.orders.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderItemDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private int quantity;
    private String productId;
    private BigDecimal price;

    public OrderItemDto() {}

    public OrderItemDto(int quantity, String productId, BigDecimal price) {
        this.quantity = quantity;
        this.productId = productId;
        this.price = price;
    }

    // Getters e Setters

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
