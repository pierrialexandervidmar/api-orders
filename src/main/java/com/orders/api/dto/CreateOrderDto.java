package com.orders.api.dto;

import java.io.Serializable;
import java.util.List;

public class CreateOrderDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long clientId;

    private List<OrderItemDto> items;
    private String cardHash;

    public CreateOrderDto() {}

    public CreateOrderDto(Long clientId, List<OrderItemDto> items, String cardHash) {
        this.clientId = clientId;
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

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}
