package com.orders.api.dto;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class CreateOrderDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String clientId;

    private List<OrderItemDto> items;
    private String cardHash;

    public CreateOrderDto() {}

    public CreateOrderDto(String clientId, List<OrderItemDto> items, String cardHash) {
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

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
