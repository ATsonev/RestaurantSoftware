package com.example.restaurantsoftware.repository;

import com.example.restaurantsoftware.model.MenuItemOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuItemOrderStatusRepository extends JpaRepository<MenuItemOrderStatus, Long> {
}
