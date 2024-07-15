package com.example.restaurantsoftware.service.impl;

import com.example.restaurantsoftware.model.Product;
import com.example.restaurantsoftware.model.customExceptions.ExistingProductException;
import com.example.restaurantsoftware.model.customExceptions.InvalidProductException;
import com.example.restaurantsoftware.model.dto.productDto.AddProductDto;
import com.example.restaurantsoftware.model.dto.productDto.AddQuantityDTO;
import com.example.restaurantsoftware.model.enums.ProductUnit;
import com.example.restaurantsoftware.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceImplTest {
    private Product testProduct;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        testProduct = new Product(){
            {
                setId(1);
                setName("Potato");
                setProductUnit(ProductUnit.KILOGRAM);
                setQuantityInStock(2);
            }
        };
    }

    @Test
    public void getProductById_ShouldReturnCorrectProduct(){
        long productId = 1;
        when(productRepository.findById(productId)).thenReturn(Optional.of(testProduct));

        Product productById = productService.getProductById(productId);

        assertNotNull(productById);
        assertEquals(productId, productById.getId());
    }

    @Test
    public void getProductById_ShouldThrowWhenProductNotPresent() {
        long productId = 1;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            productService.getProductById(productId);
        });
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    public void getAllProducts_shouldReturnAllProducts(){
        Product product2 = new Product();
        product2.setId(2);
        product2.setName("Chicken");
        List<Product> products = List.of(testProduct, product2);
        when(productRepository.findAll()).thenReturn(products);

        List<Product> allProducts = productRepository.findAll();
        assertEquals(2, allProducts.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void addProductQuantity_ShouldUpdateQuantity(){
        String name = "Potato";
        double productQuantity = testProduct.getQuantityInStock();
        double quantityToAdd = 3;
        when(productRepository.findByName(name)).thenReturn(Optional.of(testProduct));
        AddQuantityDTO dto = new AddQuantityDTO();
        dto.setName(name);
        dto.setQuantity(productQuantity);
        dto.setNewQuantity(quantityToAdd);
        dto.setId(1L);
        productService.addProductQuantity(dto);

        assertEquals(productQuantity + quantityToAdd, testProduct.getQuantityInStock());
    }

    @Test
    public void addProductQuantity_ShouldThrowWhenTheQuantityIsNegativeNumber(){
        String name = "Potato";
        double quantityToAdd = -5;
        when(productRepository.findByName(name)).thenReturn(Optional.of(testProduct));
        AddQuantityDTO dto = new AddQuantityDTO();
        dto.setName(name);
        dto.setNewQuantity(quantityToAdd);
        dto.setId(1L);

        assertThrows(InvalidProductException.class,
                () -> productService.addProductQuantity(dto));
    }

    @Test
    public void addProductQuantity_ShouldThrowWhenTheQuantityIsNotInteger(){
        Product product = new Product();
        product.setName("Lime");
        product.setProductUnit(ProductUnit.PIECE);
        double quantityToAdd = 1.5;
        when(productRepository.findByName(product.getName())).thenReturn(Optional.of(product));
        AddQuantityDTO dto = new AddQuantityDTO();
        dto.setName(product.getName());
        dto.setNewQuantity(quantityToAdd);
        dto.setId(1L);

        assertThrows(InvalidProductException.class,
                () -> productService.addProductQuantity(dto));
    }

    @Test
    public void editProductQuantity_ShouldEditQuantityCorrect(){
        String name = "Potato";
        double newQuantity = -3;
        when(productRepository.findByName(name)).thenReturn(Optional.of(testProduct));
        AddQuantityDTO dto = new AddQuantityDTO();
        dto.setName(name);
        dto.setQuantity(3);
        dto.setNewQuantity(newQuantity);
        dto.setId(1L);
        productService.editProductQuantity(dto);

        assertEquals(newQuantity, testProduct.getQuantityInStock());
    }

    @Test
    public void editProductQuantity_ShouldThrowWhenTheQuantityIsNotInteger(){
        Product product = new Product();
        product.setName("Lime");
        product.setProductUnit(ProductUnit.PIECE);
        double newQuantity = -1.5;
        when(productRepository.findByName(product.getName())).thenReturn(Optional.of(product));
        AddQuantityDTO dto = new AddQuantityDTO();
        dto.setName(product.getName());
        dto.setNewQuantity(newQuantity);
        dto.setId(1L);

        assertThrows(InvalidProductException.class,
                () -> productService.editProductQuantity(dto));
    }

    @Test
    public void addNewProduct_ShouldAddTheNewProduct(){
        String name = "Potato";
        double newQuantity = -3;
        when(productRepository.findByName(name)).thenReturn(Optional.of(testProduct));
        AddQuantityDTO dto = new AddQuantityDTO();
        dto.setName(name);
        dto.setQuantity(3);
        dto.setNewQuantity(newQuantity);
        dto.setId(1L);
        productService.editProductQuantity(dto);

        assertEquals(newQuantity, testProduct.getQuantityInStock());
    }

    @Test
    public void addNewProduct_ValidProductTest() {
        AddProductDto dto = new AddProductDto();
        dto.setName("NewProduct");
        dto.setProductUnit(ProductUnit.KILOGRAM);
        dto.setQuantityInStock(5.5);

        when(productRepository.findByName(dto.getName())).thenReturn(Optional.empty());

        Product product = new Product();
        when(modelMapper.map(dto, Product.class)).thenReturn(product);

        productService.addNewProduct(dto);

        verify(productRepository, times(1)).saveAndFlush(product);
    }

    @Test
    public void addNewProduct_ExistingProductTest() {
        AddProductDto dto = new AddProductDto();
        dto.setName(testProduct.getName());

        when(productRepository.findByName(dto.getName())).thenReturn(Optional.of(testProduct));

        assertThrows(ExistingProductException.class, () -> productService.addNewProduct(dto));

        verify(productRepository, never()).saveAndFlush(any(Product.class));
    }

    @Test
    public void addNewProduct_InvalidQuantityTest() {
        AddProductDto dto = new AddProductDto();
        dto.setName("NewProduct");
        dto.setProductUnit(ProductUnit.PIECE);
        dto.setQuantityInStock(5.5);

        when(productRepository.findByName(dto.getName())).thenReturn(Optional.empty());

        assertThrows(InvalidProductException.class, () -> productService.addNewProduct(dto));

        verify(productRepository, never()).saveAndFlush(any(Product.class));
    }

    @Test
    public void testDeleteProductById() {
        long productId = 1L;

        productService.deleteProductById(productId);

        verify(productRepository, times(1)).deleteById(productId);
    }
}
