package com.example.restaurantsoftware.service.impl;

import com.example.restaurantsoftware.model.Product;
import com.example.restaurantsoftware.model.customExceptions.ExistingProductException;
import com.example.restaurantsoftware.model.customExceptions.InvalidProductException;
import com.example.restaurantsoftware.model.dto.productDto.AddProductDto;
import com.example.restaurantsoftware.model.dto.productDto.AddQuantityDTO;
import com.example.restaurantsoftware.model.dto.productDto.ShowProductDto;
import com.example.restaurantsoftware.repository.ProductRepository;
import com.example.restaurantsoftware.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ShowProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(p -> {
                    ShowProductDto map = modelMapper.map(p, ShowProductDto.class);
                    BigDecimal bd = BigDecimal.valueOf(p.getQuantityInStock()).setScale(3, RoundingMode.HALF_UP);
                    DecimalFormat df = new DecimalFormat("#.###");
                    map.setQuantity(df.format(p.getQuantityInStock()));
                    return map;
                }).collect(Collectors.toList());
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public void addProductQuantity(AddQuantityDTO dto) {
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
    public void editProductQuantity(AddQuantityDTO dto) {
        Product product = productRepository.findByName(dto.getName()).get();
        if(product.getProductUnit().name().equals("PIECE") && Math.floor(dto.getNewQuantity()) != dto.getNewQuantity()){
            throw new InvalidProductException("The quantity should be integer number");
        }else {
            product.setQuantityInStock(dto.getNewQuantity());
            productRepository.saveAndFlush(product);
        }
    }

    @Override
    public void addNewProduct(AddProductDto dto) {
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
