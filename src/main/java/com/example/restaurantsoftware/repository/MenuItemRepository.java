package com.example.restaurantsoftware.repository;

import com.example.restaurantsoftware.model.MenuItem;
import com.example.restaurantsoftware.model.enums.MenuItemCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    Optional<MenuItem> findByName(String name);

    List<MenuItem> getMenuItemsByMenuItemCategory(MenuItemCategory category);

    Page<MenuItem> findAllByMenuItemCategory(MenuItemCategory category, Pageable pageable);

}
