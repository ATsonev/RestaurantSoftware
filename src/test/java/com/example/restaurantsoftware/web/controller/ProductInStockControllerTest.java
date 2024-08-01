package com.example.restaurantsoftware.web.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.ArrayList;

import com.example.restaurantsoftware.model.Product;
import com.example.restaurantsoftware.model.dto.productDto.AddQuantityDTO;
import com.example.restaurantsoftware.model.dto.productDto.ShowProductDto;
import com.example.restaurantsoftware.service.ProductService;
import com.example.restaurantsoftware.util.NumberFormatterUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.ui.Model;


public class ProductInStockControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private Model model;

    @InjectMocks
    private ProductInStockController productInStockController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProductsInStock() {
        List<ShowProductDto> productList = new ArrayList<>();
        when(productService.getAllProducts()).thenReturn(productList);

        String viewName = productInStockController.productsInStock(model);

        verify(productService).getAllProducts();
        verify(model).addAttribute("products", productList);
        assertEquals("products/products-in-stock", viewName);
    }

    @Test
    public void testShowAddProductQuantityPage_WithoutAttribute() {
        Long productId = 1L;
        Product product = new Product();
        product.setQuantityInStock(10.0);
        AddQuantityDTO dto = new AddQuantityDTO();

        when(model.containsAttribute("dto")).thenReturn(false);
        when(productService.getProductById(productId)).thenReturn(product);
        when(modelMapper.map(product, AddQuantityDTO.class)).thenReturn(dto);

        String viewName = productInStockController.showAddProductQuantityPage(productId, model);

        verify(model).containsAttribute("dto");
        verify(productService).getProductById(productId);
        verify(modelMapper).map(product, AddQuantityDTO.class);
        verify(model).addAttribute("dto", dto);
        assertEquals("products/add-product-quantity", viewName);
    }

    @Test
    public void testShowAddProductQuantityPage_WithAttribute() {
        Long productId = 1L;

        when(model.containsAttribute("dto")).thenReturn(true);

        String viewName = productInStockController.showAddProductQuantityPage(productId, model);

        verify(model).containsAttribute("dto");
        verify(productService, never()).getProductById(any(Long.class));
        verify(modelMapper, never()).map(any(Product.class), eq(AddQuantityDTO.class));
        verify(model, never()).addAttribute(eq("dto"), any(AddQuantityDTO.class));
        assertEquals("products/add-product-quantity", viewName);
    }

}
