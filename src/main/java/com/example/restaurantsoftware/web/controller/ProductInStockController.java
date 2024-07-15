package com.example.restaurantsoftware.web.controller;

import com.example.restaurantsoftware.model.Product;
import com.example.restaurantsoftware.model.customExceptions.ExistingProductException;
import com.example.restaurantsoftware.model.customExceptions.InvalidProductException;
import com.example.restaurantsoftware.model.dto.productDto.AddProductDto;
import com.example.restaurantsoftware.model.dto.productDto.AddQuantityDTO;
import com.example.restaurantsoftware.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductInStockController {

    private final ProductService productService;
    private final ModelMapper modelMapper;

    public ProductInStockController(ProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/products-in-stock")
    public String productsInStock(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "products/products-in-stock";
    }

    @GetMapping("/add-product-quantity/{id}")
    public String showAddProductQuantityPage(@PathVariable Long id, Model model) {
        if(!model.containsAttribute("dto")){
            Product product = productService.getProductById(id);
            AddQuantityDTO dto = modelMapper.map(product, AddQuantityDTO.class);
            model.addAttribute("dto", dto);
        }
        return "products/add-product-quantity";
    }

    @PostMapping("/add-product-quantity")
    public String addProductQuantity(@ModelAttribute("dto") AddQuantityDTO dto,
                                     RedirectAttributes redirectAttributes) {
        try {
            productService.addProductQuantity(dto);
        }catch (InvalidProductException e){
            redirectAttributes.addFlashAttribute("dto", dto);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/products/add-product-quantity/" + dto.getId();
        }
        return "redirect:/products/products-in-stock";
    }

    @GetMapping("/edit-product-quantity/{id}")
    public String showEditProductQuantityPage(Model model, @PathVariable Long id) {
        if(!model.containsAttribute("dto")) {
            Product product = productService.getProductById(id);
            AddQuantityDTO dto = modelMapper.map(product, AddQuantityDTO.class);
            model.addAttribute("dto", dto);
        }
        return "products/edit-product-quantity";
    }

    @PostMapping("/edit-product-quantity")
    public String editProductQuantity(AddQuantityDTO dto,
                                      RedirectAttributes redirectAttributes) {
        try{
            productService.editProductQuantity(dto);
        }catch (InvalidProductException e){
            redirectAttributes.addFlashAttribute("dto", dto);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/products/edit-product-quantity/" + dto.getId();
        }
        return "redirect:/products/products-in-stock";
    }

    @GetMapping ("/add-product")
    public String showAddProduct(Model model) {
        if(!model.containsAttribute("dto")){
            model.addAttribute("dto", new AddProductDto());
        }
        return "products/add-product";
    }

    @PostMapping("/add-product")
    public String addProduct(@Valid @ModelAttribute("dto") AddProductDto dto,
                             BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("dto", dto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.dto", bindingResult);
            return "redirect:/products/add-product";
        }else {
            try{
                productService.addNewProduct(dto);
            }catch (InvalidProductException e){
                return getString(dto, redirectAttributes, e, "errorMessage");
            }catch (ExistingProductException e){
                return getString(dto, redirectAttributes, e, "nameErrorMassage");
            }
            return "redirect:/products/products-in-stock";
        }
    }

    @DeleteMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable (value = "id") Long id){
        this.productService.deleteProductById(id);
        return "redirect:/products/products-in-stock";
    }

    private static String getString(AddProductDto dto, RedirectAttributes redirectAttributes, RuntimeException e, String attribute) {
        redirectAttributes.addFlashAttribute(attribute, e.getMessage());
        redirectAttributes.addFlashAttribute("dto", dto);
        return "redirect:/products/add-product";
    }

}
