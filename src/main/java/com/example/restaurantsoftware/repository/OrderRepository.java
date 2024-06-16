package com.example.restaurantsoftware.repository;

import com.example.restaurantsoftware.model.Order;
import com.example.restaurantsoftware.model.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByTable(Table table);

}
