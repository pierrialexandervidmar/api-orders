package com.orders.api.service;

import com.orders.api.dto.CreateOrderDto;
import com.orders.api.dto.OrderItemResponse;
import com.orders.api.dto.OrderResponse;
import com.orders.api.entity.Order;
import com.orders.api.entity.OrderItem;
import com.orders.api.entity.Product;
import com.orders.api.enums.OrderStatus;
import com.orders.api.repository.OrderRepository;
import com.orders.api.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.query.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Classe de serviço responsável por gerenciar as operações relacionadas a Pedidos.
 * <p>
 * Author: Pierri Alexander Vidmar
 * Since: 05/2025
 */
@Service
public class OrderService {

    @Autowired
    private final OrderRepository orderRepository;

    @Autowired
    private final ProductRepository productRepository;

    public List<OrderResponse> findAll() {
        return orderRepository.findAllWithItems()
                .stream()
                .map(order -> mapToResponse(order))
                .collect(Collectors.toList());
    }


    /**
     * Construtor para injeção de dependências.
     *
     * @param orderRepository   Repositório para a entidade Order.
     * @param productRepository Repositório para a entidade Product.
     */
    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    /**
     * Cria um novo pedido com os dados fornecidos.
     * Associa os produtos aos itens do pedido e calcula o valor total.
     *
     * @param dto Objeto contendo os itens do pedido.
     * @return O pedido criado e persistido.
     * @throws RuntimeException Se algum produto informado não for encontrado.
     */
    public Order create(CreateOrderDto dto) {
        Order order = new Order();
        order.setClientId(dto.getClientId());

        List<OrderItem> items = dto.getItems().stream().map(input -> {
            OrderItem item = new OrderItem();

            Product product = productRepository.findById(input.getProductId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + input.getProductId()));

            item.setProduct(product);
            item.setQuantity(input.getQuantity());
            item.setPrice(input.getPrice());
            item.setOrder(order);
            return item;
        }).toList();

        order.setItems(items);
        order.setTotal(order.calculateTotal());

        return orderRepository.save(order);
    }

    /**
     * Processa o pagamento de um pedido.
     * Apenas pedidos com status PENDING podem ser pagos.
     *
     * @param orderId UUID do pedido a ser pago.
     * @return O pedido atualizado com status PAID.
     * @throws IllegalStateException   Se o pedido não estiver no status PENDING.
     * @throws EntityNotFoundException Se o pedido não for encontrado.
     */
    public Order pay(String orderId) {
        Order order = findById(orderId);
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("O pedido não pode ser pago");
        }
        order.setStatus(OrderStatus.PAID);
        return orderRepository.save(order);
    }

    /**
     * Marca um pedido como falhado.
     * Apenas pedidos com status PENDING podem ser marcados como FAILED.
     *
     * @param orderId UUID do pedido a ser marcado como falhado.
     * @return O pedido atualizado com status FAILED.
     * @throws IllegalStateException   Se o pedido não estiver no status PENDING.
     * @throws EntityNotFoundException Se o pedido não for encontrado.
     */
    public Order fail(String orderId) {
        Order order = findById(orderId);
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("O pedido não pode ser marcado como falhado");
        }
        order.setStatus(OrderStatus.FAILED);
        return orderRepository.save(order);
    }

    /**
     * Busca um pedido pelo seu UUID.
     *
     * @param id UUID do pedido.
     * @return O pedido encontrado.
     * @throws EntityNotFoundException Se o pedido não for encontrado.
     */
    public Order findById(String id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));
    }


    private OrderResponse mapToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setClientId(order.getClientId());
        response.setTotal(order.getTotal());
        response.setCreatedAt(order.getCreatedAt());

        List<OrderItemResponse> items = order.getItems().stream()
                .map(this::mapToItemResponse)
                .collect(Collectors.toList());

        response.setItems(items);
        return response;
    }

    private OrderItemResponse mapToItemResponse(OrderItem item) {
        OrderItemResponse response = new OrderItemResponse();
        response.setId(item.getId());
        response.setQuantity(item.getQuantity());
        response.setPrice(item.getPrice());
        response.setProductId(item.getProduct().getId());
        return response;
    }
}
