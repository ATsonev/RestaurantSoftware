package com.example.restaurantsoftware.service.impl;

import com.example.restaurantsoftware.model.Product;
import com.example.restaurantsoftware.model.customExceptions.ExistingProductException;
import com.example.restaurantsoftware.model.customExceptions.InvalidProductException;
import com.example.restaurantsoftware.model.dto.productDto.AddProductDto;
import com.example.restaurantsoftware.model.dto.productDto.AddQuantityDTO;
import com.example.restaurantsoftware.model.enums.ProductUnit;
import com.example.restaurantsoftware.repository.ProductRepository;
import com.example.restaurantsoftware.service.ProductService;
import com.example.restaurantsoftware.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ValidationUtil validator;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRepository, ValidationUtil validator, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.validator = validator;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProduct(Long id) {
        return productRepository.getById(id);
    }

    @Override
    public void updateProductQuantity(AddQuantityDTO dto) {
        Product product = productRepository.findByName(dto.getName()).get();
        if(dto.getNewQuantity() < 0){
            throw new InvalidProductException("The quantity should be positive number");
        }else if(product.getProductUnit().name().equals("PIECE") && Math.floor(dto.getNewQuantity()) != dto.getNewQuantity()){
            throw new InvalidProductException("The quantity should be integer number");
        }else{
            double setQuantity = product.getQuantityInStock() + dto.getNewQuantity();
            product.setQuantityInStock(setQuantity);
            productRepository.saveAndFlush(product);
        }
    }

    @Override
    public void editProductQuantity(String productName, double newQuantity) {
        Product product = productRepository.findByName(productName).get();
        product.setQuantityInStock(newQuantity);
        productRepository.saveAndFlush(product);
    }

    @Override
    public void addProduct(AddProductDto dto) {
        Optional<Product> byName = productRepository.findByName(dto.getName());
        if(byName.isPresent()){
            throw new ExistingProductException("Product with such name already exist");
        }else if(dto.getProductUnit().name().equals("PIECE") && Math.floor(dto.getQuantityInStock()) != dto.getQuantityInStock()){
            throw new InvalidProductException("The quantity should be integer number");
        }else{
            Product product = modelMapper.map(dto, Product.class);
            productRepository.saveAndFlush(product);
        }
    }

    @Override
    public void deleteProductById(long id) {
        productRepository.deleteById(id);
    }
}
