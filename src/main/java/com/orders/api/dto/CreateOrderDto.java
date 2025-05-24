package com.orders.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.List;

public class CreateOrderDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Campo de cliente é obrigatório")
    private String clientId;

    @NotNull(message = "Itens obrigatórios")
    @NotEmpty(message = "A lista de itens não pode estar vazia")
    private List<OrderItemDto> items;

    private String cardHash;

    public CreateOrderDto() {
    }

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
