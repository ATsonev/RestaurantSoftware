package com.example.restaurantsoftware.web.controller;

import com.example.restaurantsoftware.model.MenuItem;
import com.example.restaurantsoftware.model.Table;
import com.example.restaurantsoftware.model.Waiter;
import com.example.restaurantsoftware.model.dto.menuItemDto.ShowMenuItemJSONDTo;
import com.example.restaurantsoftware.model.dto.orderDto.DeleteOrderItemDTO;
import com.example.restaurantsoftware.model.dto.orderDto.MenuItemsDto;
import com.example.restaurantsoftware.service.MenuItemService;
import com.example.restaurantsoftware.service.OrderService;
import com.example.restaurantsoftware.service.TableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrdersController.class)
public class OrdersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuItemService menuItemService;

    @MockBean
    private OrderService orderService;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private TableService tableService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(username = "user", roles = {"WAITER"})
    public void testGetMenuItemsByCategory() throws Exception {
        ShowMenuItemJSONDTo menuItem = new ShowMenuItemJSONDTo();
        Page<ShowMenuItemJSONDTo> page = new PageImpl<>(List.of(menuItem), PageRequest.of(0, 10), 1);

        when(menuItemService.getMenuItemsByCategory(any(String.class), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/menu-items")
                        .param("category", "coffee")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(username = "user", roles = {"WAITER"})
    public void testTableOrder() throws Exception {
        MenuItem menuItem = new MenuItem();
        menuItem.setName("Cola");
        List<MenuItem> menuItems = List.of(menuItem);
        ShowMenuItemJSONDTo dto = new ShowMenuItemJSONDTo(menuItem.getName());

        Waiter waiter = new Waiter();
        waiter.setId(1L);
        Table table = new Table();
        table.setId(1);
        table.setWaiter(waiter);

        MenuItemsDto currentOrder = new MenuItemsDto();
        List<MenuItemsDto> currentOrders = Collections.singletonList(currentOrder);

        when(menuItemService.getAllMenuItems()).thenReturn(menuItems);
        when(tableService.getTableById(anyLong())).thenReturn(table);
        when(orderService.getCurrentOrdersForTable(anyLong())).thenReturn(currentOrders);
        when(tableService.getAllTables()).thenReturn(Collections.emptyList());
        when(modelMapper.map(menuItem, ShowMenuItemJSONDTo.class)).thenReturn(dto);

        mockMvc.perform(get("/table1-order/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("menuItems"))
                .andExpect(model().attributeExists("currentOrders"))
                .andExpect(model().attributeExists("tables"))
                .andExpect(model().attribute("tableWaiterId", waiter.getId()))
                .andExpect(model().attribute("menuItems", List.of(dto)))
                .andExpect(view().name("table-order"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"WAITER"})
    public void test_setWaiter() throws Exception {
        doNothing().when(tableService).setWaiter(anyLong(), anyLong());

        mockMvc.perform(get("/order/setWaiter/1/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/table1-order/1"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"WAITER"})
    public void testMakeOrder() throws Exception {
        MenuItemsDto orderItem = new MenuItemsDto();
        List<MenuItemsDto> orderItems = Collections.singletonList(orderItem);

        doNothing().when(orderService).makeOrder(anyLong(), anyLong(), anyList());

        mockMvc.perform(post("/order/makeOrder/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"menuItem\":\"Item1\", \"quantity\":2}]")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"redirect\":\"/table1-order/1\"}"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"WAITER"})
    public void testDeleteOrderItem_Success() throws Exception {

        when(orderService.deleteOrderItem(any(DeleteOrderItemDTO.class))).thenReturn(true);

        mockMvc.perform(post("/order/deleteOrderItem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orderItemId\": 1}")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"WAITER"})
    public void testDeleteOrderItem_Failure() throws Exception {

        when(orderService.deleteOrderItem(any(DeleteOrderItemDTO.class))).thenReturn(false);

        mockMvc.perform(post("/order/deleteOrderItem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orderItemId\": 1}")
                        .with(csrf()))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = "user", roles = {"WAITER"})
    public void testFinishTableWithCard_Success() throws Exception {
        when(orderService.finishTable(anyLong(), anyLong(), anyString(), anyDouble())).thenReturn(true);

        mockMvc.perform(post("/order/finishTableCard/1/1")
                        .param("discount", "10.0")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Payment completed successfully!"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"WAITER"})
    public void testFinishTableWithCard_PendingOrders() throws Exception {
        when(orderService.finishTable(anyLong(), anyLong(), anyString(), anyDouble())).thenReturn(false);

        mockMvc.perform(post("/order/finishTableCard/1/1")
                        .param("discount", "10.0")
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("The table has pending orders."));
    }

}
