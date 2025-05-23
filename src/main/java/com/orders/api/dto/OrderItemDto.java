package com.orders.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderItemDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Min(1)
    private int quantity;

    @NotBlank
    private String productId;

    @NotNull
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
