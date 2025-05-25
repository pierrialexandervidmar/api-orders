package com.orders.api.repository;

import com.orders.api.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {

    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.items")
    List<Order> findAllWithItems();

    @Query("SELECT o FROM Order o WHERE o.clientId = :clientId")
    List<Order> findByClientId(@Param("clientId") String clientId);
}
