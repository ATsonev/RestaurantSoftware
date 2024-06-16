package com.example.restaurantsoftware.service;


import com.example.restaurantsoftware.model.Product;
import com.example.restaurantsoftware.model.dto.productDto.AddProductDto;
import com.example.restaurantsoftware.model.dto.productDto.AddQuantityDTO;

import java.util.List;

public interface ProductService {

    List<Product> getAllProducts();
    Product getProduct(Long id);
    void updateProductQuantity(AddQuantityDTO dto);

    void editProductQuantity(String productName, double newQuantity);
    void addProduct(AddProductDto dto);
    void deleteProductById(long id);
}
