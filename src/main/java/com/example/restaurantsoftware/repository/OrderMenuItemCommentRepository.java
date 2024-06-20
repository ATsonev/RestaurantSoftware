package com.example.restaurantsoftware.repository;

import com.example.restaurantsoftware.model.MenuItem;
import com.example.restaurantsoftware.model.Order;
import com.example.restaurantsoftware.model.OrderMenuItemComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderMenuItemCommentRepository extends JpaRepository<OrderMenuItemComment, Long> {
     Optional<OrderMenuItemComment> findByOrderAndMenuItem(Order order, MenuItem menuItem);
}
