package com.example.restaurantsoftware.service;


import com.example.restaurantsoftware.model.Product;
import com.example.restaurantsoftware.model.dto.productDto.AddProductDto;
import com.example.restaurantsoftware.model.dto.productDto.AddQuantityDTO;

import java.util.List;

public interface ProductService {

    List<Product> getAllProducts();
    Product getProductById(Long id);
    void addProductQuantity(AddQuantityDTO dto);
    void editProductQuantity(AddQuantityDTO dto);
    void addNewProduct(AddProductDto dto);
    void deleteProductById(long id);

}
