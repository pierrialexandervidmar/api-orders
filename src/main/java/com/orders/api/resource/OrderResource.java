package com.orders.api.resource;

import com.orders.api.dto.CreateOrderDto;
import com.orders.api.dto.OrderResponse;
import com.orders.api.entity.Order;
import com.orders.api.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderResource {

    private final OrderService orderService;

    @Autowired
    public OrderResource(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Order create(@Valid @RequestBody CreateOrderDto createOrderDto) {
        return orderService.create(createOrderDto);
    }

    @GetMapping
    public List<OrderResponse> findAll() {
        return orderService.findAll();
    }

    @GetMapping("/client/{id}")
    public List<OrderResponse> findByClientId(@PathVariable String id) {
        return orderService.findByClientId(id);
    }
//
//    @GetMapping("/{id}")
//    public Order findOne(@PathVariable UUID id) {
//        return orderService.findOne(id);
//    }

//    @PutMapping("/{id}")
//    public Order update(@PathVariable UUID id, @RequestBody OrderDto updateOrderDto) {
//        return orderService.update(id, updateOrderDto);
//    }
//
//    @DeleteMapping("/{id}")
//    public void delete(@PathVariable UUID id) {
//        orderService.delete(id);
//    }
}
