package com.example.restaurantsoftware.repository;

import com.example.restaurantsoftware.model.MenuItemProductQuantity;
import com.example.restaurantsoftware.model.Product;
import com.example.restaurantsoftware.model.enums.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MenuItemProductQuantityRepository extends JpaRepository<MenuItemProductQuantity, Long> {

    Optional<MenuItemProductQuantity> findByProductAndQuantityAndProductCategory(Product product, double quantity, ProductCategory productCategory);

}
