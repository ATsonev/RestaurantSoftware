package com.example.restaurantsoftware.web.controller;

import com.example.restaurantsoftware.model.Product;
import com.example.restaurantsoftware.model.customExceptions.ExistingProductException;
import com.example.restaurantsoftware.model.customExceptions.InvalidProductException;
import com.example.restaurantsoftware.model.dto.productDto.AddProductDto;
import com.example.restaurantsoftware.model.dto.productDto.AddQuantityDTO;
import com.example.restaurantsoftware.model.enums.ProductUnit;
import com.example.restaurantsoftware.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ProductService productService;
    private final ModelMapper modelMapper;

    public ProductInStockController(ModelMapper modelMapper) {
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
            Product product = productService.getProduct(id);
            AddQuantityDTO dto = modelMapper.map(product, AddQuantityDTO.class);
            model.addAttribute("dto", dto);
        }
        return "products/add-product-quantity";
    }

    @PostMapping("/add-product-quantity")
    public String addProductQuantity(@ModelAttribute("dto") AddQuantityDTO dto,
                                     RedirectAttributes redirectAttributes) {
        try {
            productService.updateProductQuantity(dto);
        }catch (InvalidProductException e){
            redirectAttributes.addFlashAttribute("dto", dto);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/products/add-product-quantity/" + dto.getId();
        }
        return "redirect:/products/products-in-stock";
    }

    @GetMapping("/edit-product-quantity")
    public String showEditProductQuantityPage(@RequestParam("name") String productName,
                                             @RequestParam("quantity") double currentQuantity,
                                             Model model) {
        model.addAttribute("productName", productName);
        model.addAttribute("currentQuantity", currentQuantity);
        return "products/edit-product-quantity";
    }

    @PostMapping("/edit-product-quantity")
    public String editProductQuantity(@RequestParam("name") String productName,
                                     @RequestParam("newQuantity") double newQuantity) {
        productService.editProductQuantity(productName, newQuantity);
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
                productService.addProduct(dto);
            }catch (InvalidProductException e){
                return getString(dto, redirectAttributes, e, "errorMessage");
            }catch (ExistingProductException e){
                return getString(dto, redirectAttributes, e, "nameErrorMassage");
            }
            return "redirect:/products/products-in-stock";
        }
    }

    @GetMapping("/deleteProduct/{id}")
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
