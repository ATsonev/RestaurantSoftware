package com.example.restaurantsoftware.repository;

import com.example.restaurantsoftware.model.OrderMenuItemComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMenuItemCommentRepository extends JpaRepository<OrderMenuItemComment, Long> {
}
