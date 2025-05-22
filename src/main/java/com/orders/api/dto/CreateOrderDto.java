package com.orders.api.dto;

import java.io.Serializable;
import java.util.List;

public class CreateOrderDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<OrderItemDto> items;
    private String cardHash;

    public CreateOrderDto() {}

    public CreateOrderDto(List<OrderItemDto> items, String cardHash) {
        this.items = items;
        this.cardHash = cardHash;
    }

    public List<OrderItemDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDto> items) {
        this.items = items;
    }

    public String getCardHash() {
        return cardHash;
    }

    public void setCardHash(String cardHash) {
        this.cardHash = cardHash;
    }

}
