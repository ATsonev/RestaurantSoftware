package com.example.restaurantsoftware.service.impl;

import com.example.restaurantsoftware.model.MenuItem;
import com.example.restaurantsoftware.model.MenuItemProductQuantity;
import com.example.restaurantsoftware.model.Product;
import com.example.restaurantsoftware.model.customExceptions.InvalidProductException;
import com.example.restaurantsoftware.model.dto.menuItemDto.*;
import com.example.restaurantsoftware.model.enums.MenuItemCategory;
import com.example.restaurantsoftware.model.enums.ProductCategory;
import com.example.restaurantsoftware.model.enums.ProductUnit;
import com.example.restaurantsoftware.model.enums.VAT;
import com.example.restaurantsoftware.repository.MenuItemRepository;
import com.example.restaurantsoftware.repository.MenuItemProductQuantityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MenuItemServiceImplTest {
    @Mock
    private MenuItemRepository menuItemRepository;
    @Mock
    private MenuItemProductQuantityRepository menuItemProductQuantityRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private MenuItemServiceImpl menuItemService;
    private MenuItem menuItem;
    private MenuItemProductQuantity menuItemProductQuantity;
    private Product product;

    private AddMenuItemDTO addMenuItemDTO;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        long menuItemId = 1;
        menuItem = new MenuItem();
        menuItem.setId(menuItemId);
        menuItem.setName("Pizza");
        menuItem.setDescription("Old Description");
        menuItem.setMenuItemCategory(MenuItemCategory.PIZZAS);
        menuItem.setPrice(10.0);
        menuItem.setVat(VAT.VAT0);
        menuItem.setImage("Old Image Content".getBytes());

        product = new Product();
        product.setName("Flour");
        product.setProductUnit(ProductUnit.KILOGRAM);
        product.setQuantityInStock(5);

        menuItemProductQuantity = new MenuItemProductQuantity();
        menuItemProductQuantity.setQuantity(0.5);
        menuItemProductQuantity.setProduct(product);
        menuItemProductQuantity.setMenuItems(new HashSet<>(Set.of(menuItem)));

        menuItem.setMenuItemProductsQuantity(new HashSet<>(Set.of(menuItemProductQuantity)));

        MockMultipartFile image = new MockMultipartFile("image", "pizza.jpg", "image/jpeg", "Pizza Image".getBytes());
        addMenuItemDTO = new AddMenuItemDTO();
        addMenuItemDTO.setName("Pizza");
        addMenuItemDTO.setDescription("Delicious cheese pizza");
        addMenuItemDTO.setPrice(10.0);
        addMenuItemDTO.setImage(image);
    }

    @Test
    public void testEditMenuItem_WithImage() throws IOException {
        EditMenuItemDTO dto = new EditMenuItemDTO();
        dto.setId(1L);
        dto.setName("New Name");
        dto.setDescription("New Description");
        dto.setMenuItemCategory(MenuItemCategory.SALADS);
        dto.setPrice(15.0);
        dto.setVat(VAT.VAT20);
        MockMultipartFile image = new MockMultipartFile("image", "", "image/jpeg", new byte[0]);
        dto.setImage(image);

        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));
        when(menuItemRepository.saveAndFlush(any(MenuItem.class))).thenReturn(menuItem);

        menuItemService.editMenuItem(dto);

        verify(menuItemRepository, times(1)).findById(1L);
        verify(menuItemRepository, times(1)).saveAndFlush(menuItem);

        assertEquals("New Name", menuItem.getName());
        assertEquals("New Description", menuItem.getDescription());
        assertEquals(MenuItemCategory.SALADS, menuItem.getMenuItemCategory());
        assertEquals(15.0, menuItem.getPrice());
        assertEquals(VAT.VAT20, menuItem.getVat());
        assertEquals("Old Image Content", new String(menuItem.getImage()));

        MockMultipartFile newImage = new MockMultipartFile("image", "image.jpg", "image/jpeg", "Test Image Content".getBytes());
        dto.setImage(newImage);
        menuItemService.editMenuItem(dto);

        assertEquals("Test Image Content", new String(newImage.getBytes()));
    }

    @Test
    public void testRemoveProduct_Success() {
        when(menuItemProductQuantityRepository.findById(menuItemProductQuantity.getId()))
                .thenReturn(Optional.of(menuItemProductQuantity));
        when(menuItemRepository.findById(menuItem.getId())).thenReturn(Optional.of(menuItem));

        menuItemService.removeProduct(menuItemProductQuantity.getId(), menuItem.getId());

        verify(menuItemRepository, times(1)).save(menuItem);
        verify(menuItemProductQuantityRepository, times(1))
                .delete(menuItemProductQuantity);
        assertFalse(menuItem.getMenuItemProductsQuantity().contains(menuItemProductQuantity));
    }

    @Test
    public void testRemoveProduct_ShouldNotRemoveTheProductQuantity() {
        Set<MenuItem> menuItems = menuItemProductQuantity.getMenuItems();
        menuItems.add(new MenuItem("Cookies", 4));
        when(menuItemProductQuantityRepository.findById(menuItemProductQuantity.getId()))
                .thenReturn(Optional.of(menuItemProductQuantity));
        when(menuItemRepository.findById(menuItem.getId())).thenReturn(Optional.of(menuItem));

        menuItemService.removeProduct(menuItemProductQuantity.getId(), menuItem.getId());

        verify(menuItemRepository, times(1)).save(menuItem);
        verify(menuItemProductQuantityRepository, never()).delete(menuItemProductQuantity);
        assertFalse(menuItem.getMenuItemProductsQuantity().contains(menuItemProductQuantity));
    }

    @Test
    public void testAddProduct_NewProduct() {
        MenuItemAddProductDTO dto = new MenuItemAddProductDTO();
        dto.setMenuItemId(menuItem.getId());
        dto.setProduct(product);
        dto.setQuantity(0.6);
        dto.setProductCategory(ProductCategory.PIZZA);

        when(menuItemRepository.findById(menuItem.getId())).thenReturn(Optional.of(menuItem));
        when(menuItemProductQuantityRepository.findByProductAndQuantityAndProductCategory(
                dto.getProduct(), dto.getQuantity(), dto.getProductCategory()
        )).thenReturn(Optional.empty());

        menuItemService.addProduct(dto);

        verify(menuItemRepository, times(1)).save(menuItem);
        verify(menuItemProductQuantityRepository, times(1))
                .saveAndFlush(any(MenuItemProductQuantity.class));
        assertEquals(2, menuItem.getMenuItemProductsQuantity().size());
    }

    @Test
    public void testAddProduct_WithExistingProductQuantity() {
        MenuItemAddProductDTO dto = new MenuItemAddProductDTO();
        dto.setMenuItemId(menuItem.getId());
        dto.setProduct(product);
        dto.setQuantity(0.6);
        dto.setProductCategory(ProductCategory.PIZZA);

        when(menuItemRepository.findById(menuItem.getId())).thenReturn(Optional.of(menuItem));
        when(menuItemProductQuantityRepository.findByProductAndQuantityAndProductCategory(
                dto.getProduct(), dto.getQuantity(), dto.getProductCategory()
        )).thenReturn(Optional.of(menuItemProductQuantity));

        menuItemService.addProduct(dto);

        verify(menuItemRepository, times(1)).save(menuItem);
        verify(menuItemProductQuantityRepository, never())
                .saveAndFlush(any(MenuItemProductQuantity.class));
        assertEquals(1, menuItem.getMenuItemProductsQuantity().size());
    }

    @Test
    public void testAddProduct_InvalidProductQuantityForPiece() {
        MenuItemAddProductDTO dto = new MenuItemAddProductDTO();
        dto.setMenuItemId(menuItem.getId());
        product.setProductUnit(ProductUnit.PIECE);
        dto.setProduct(product);
        dto.setQuantity(2.5);
        dto.setProductCategory(ProductCategory.PIZZA);

        when(menuItemRepository.findById(menuItem.getId())).thenReturn(Optional.of(menuItem));

        assertThrows(InvalidProductException.class, () -> {
            menuItemService.addProduct(dto);
        });

        verify(menuItemProductQuantityRepository, never()).saveAndFlush(any(MenuItemProductQuantity.class));
        verify(menuItemRepository, never()).save(menuItem);
    }

    @Test
    public void testSave_ValidUpdate() {
        EditMenuItemProductsDTO menuItemForm = new EditMenuItemProductsDTO();
        menuItemForm.setId(menuItem.getId());
        menuItemForm.setName(menuItem.getName());

        ProductQuantityDTO productQuantityDTO = new ProductQuantityDTO();
        productQuantityDTO.setProductName(product.getName());
        productQuantityDTO.setQuantity(3.0);
        productQuantityDTO.setProductCategory(ProductCategory.COLD_KITCHEN);
        productQuantityDTO.setProductUnit(ProductUnit.KILOGRAM);

        menuItemForm.setProductQuantities(List.of(productQuantityDTO));

        when(menuItemRepository.findById(menuItem.getId()))
                .thenReturn(Optional.of(menuItem));

        menuItemService.save(menuItemForm);

        verify(menuItemRepository, times(1)).saveAndFlush(menuItem);
        assertEquals(3.0, menuItemProductQuantity.getQuantity());
        assertEquals(ProductCategory.COLD_KITCHEN, menuItemProductQuantity.getProductCategory());
    }
    @Test
    public void testSave_InvalidProductQuantityForPiece() {
        EditMenuItemProductsDTO menuItemForm = new EditMenuItemProductsDTO();
        menuItemForm.setId(menuItem.getId());
        menuItemForm.setName(menuItem.getName());

        ProductQuantityDTO productQuantityDTO = new ProductQuantityDTO();
        productQuantityDTO.setProductName(product.getName());
        productQuantityDTO.setQuantity(2.5);
        productQuantityDTO.setProductCategory(ProductCategory.PIZZA);
        productQuantityDTO.setProductUnit(ProductUnit.PIECE);

        menuItemForm.setProductQuantities(List.of(productQuantityDTO));

        when(menuItemRepository.findById(menuItem.getId()))
                .thenReturn(Optional.of(menuItem));

        assertThrows(InvalidProductException.class, () -> {
            menuItemService.save(menuItemForm);
        });
        verify(menuItemRepository, never()).saveAndFlush(any(MenuItem.class));
    }

    @Test
    public void testGetEditProductDto() {
        long menuItemId = 1L;

        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(menuItem));

        EditMenuItemProductsDTO dto = new EditMenuItemProductsDTO();
        dto.setId(menuItem.getId());
        dto.setName(menuItem.getName());

        ProductQuantityDTO productQuantityDTO = new ProductQuantityDTO();
        productQuantityDTO.setProductName(product.getName());
        productQuantityDTO.setQuantity(menuItemProductQuantity.getQuantity());

        when(modelMapper.map(menuItem, EditMenuItemProductsDTO.class)).thenReturn(dto);
        when(modelMapper.map(menuItemProductQuantity, ProductQuantityDTO.class)).thenReturn(productQuantityDTO);

        EditMenuItemProductsDTO result = menuItemService.getEditProductDto(menuItemId);

        assertEquals(menuItem.getId(), result.getId());
        assertEquals(menuItem.getName(), result.getName());
        assertEquals(1, result.getProductQuantities().size());
        assertEquals(product.getName(), result.getProductQuantities().get(0).getProductName());
        assertEquals(menuItemProductQuantity.getQuantity(), result.getProductQuantities().get(0).getQuantity());
    }

    @Test
    public void testAddMenuItem() throws IOException {
        when(modelMapper.map(addMenuItemDTO, MenuItem.class)).thenReturn(menuItem);

        menuItemService.addMenuItem(addMenuItemDTO);

        verify(modelMapper, times(1)).map(addMenuItemDTO, MenuItem.class);
        verify(menuItemRepository, times(1)).saveAndFlush(menuItem);
        assertEquals("Pizza Image", new String(menuItem.getImage()));
    }
}
