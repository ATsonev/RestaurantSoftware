package com.example.restaurantsoftware.service.impl;

import com.example.restaurantsoftware.model.*;
import com.example.restaurantsoftware.model.dto.orderDto.*;
import com.example.restaurantsoftware.model.enums.MenuItemCategory;
import com.example.restaurantsoftware.model.enums.OrderStatus;
import com.example.restaurantsoftware.model.enums.TableStatus;
import com.example.restaurantsoftware.model.enums.VAT;
import com.example.restaurantsoftware.repository.*;
import com.example.restaurantsoftware.service.TableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private MenuItemRepository menuItemRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private WaiterRepository waiterRepository;
    @Mock
    private MenuItemOrderStatusRepository menuItemOrderStatusRepository;
    @Mock
    private TableRepository tableRepository;
    @Mock
    private BillRepository billRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private TableService tableService;
    @InjectMocks
    private OrderServiceImpl orderService;
    private MenuItemOrderStatus menuItemOrderStatus1;
    private MenuItemOrderStatus menuItemOrderStatus2;
    private MenuItem menuItem1;
    private MenuItem menuItem2;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);

        menuItem1 = new MenuItem();
        menuItem1.setName("Pizza");
        menuItem1.setPrice(10.0);
        menuItem1.setMenuItemCategory(MenuItemCategory.PIZZAS);

        menuItem2 = new MenuItem();
        menuItem2.setName("Salad");
        menuItem2.setMenuItemCategory(MenuItemCategory.SALADS);
        menuItem2.setPrice(5.0);

        this.menuItemOrderStatus1 = new MenuItemOrderStatus(menuItem1, OrderStatus.PENDING);
        menuItemOrderStatus1.setId(1L);
        this.menuItemOrderStatus2 = new MenuItemOrderStatus(menuItem2, OrderStatus.PENDING);
        menuItemOrderStatus2.setId(2L);
    }


    @Test
    public void testMakeOrder() {
        long waiterId = 1L;
        long tableId = 1L;

        Waiter waiter = new Waiter();
        waiter.setId(waiterId);
        Table table = new Table();
        table.setId(tableId);
        Order order = new Order();
        order.setId(1L);
        order.setDateAndTimeOrdered(LocalDateTime.now());
        order.setWaiter(waiter);
        order.setTable(table);
        order.setOrderStatus(OrderStatus.PENDING);

        MenuItemsDto item1 = new MenuItemsDto();
        item1.setMenuItem("Pizza");
        item1.setQuantity(2);
        item1.setComment("Extra cheese");

        MenuItemsDto item2 = new MenuItemsDto();
        item2.setMenuItem("Salad");
        item2.setQuantity(3);
        item2.setComment("");

        List<MenuItemsDto> orderItems = Arrays.asList(item1, item2);

        MenuItem menuItem1 = new MenuItem();
        menuItem1.setName("Pizza");
        MenuItem menuItem2 = new MenuItem();
        menuItem2.setName("Salad");

        when(waiterRepository.findById(waiterId)).thenReturn(Optional.of(waiter));
        when(tableRepository.findById(tableId)).thenReturn(Optional.of(table));
        when(orderRepository.saveAndFlush(any(Order.class))).thenReturn(order);
        when(menuItemRepository.findByName("Pizza")).thenReturn(Optional.of(menuItem1));
        when(menuItemRepository.findByName("Salad")).thenReturn(Optional.of(menuItem2));

        orderService.makeOrder(waiterId, tableId, orderItems);

        verify(orderRepository, times(1)).saveAndFlush(any(Order.class));
        verify(menuItemOrderStatusRepository, times(1)).saveAll(any(Iterable.class));
    }

    @Test
    public void testGetCurrentOrdersForTable() {
        Long tableId = 1L;

        Table table = new Table();
        table.setId(tableId);

        MenuItemOrderStatus menuItemOrderStatus3 = new MenuItemOrderStatus(menuItem1, OrderStatus.PENDING);
        Order order = new Order();
        order.setMenuItems(Arrays.asList(menuItemOrderStatus1, menuItemOrderStatus2, menuItemOrderStatus3));

        table.setOrders(List.of(order));

        when(tableRepository.findById(tableId)).thenReturn(Optional.of(table));

        List<MenuItemsDto> result = orderService.getCurrentOrdersForTable(tableId);

        assertEquals(2, result.size());
        assertEquals("Pizza", result.get(0).getMenuItem());
        assertEquals(2, result.get(0).getQuantity());
        assertEquals(10.0, result.get(0).getPrice());
        assertEquals("Salad", result.get(1).getMenuItem());
        assertEquals(1, result.get(1).getQuantity());
        assertEquals(5.0, result.get(1).getPrice());

        verify(tableRepository, times(1)).findById(tableId);
    }

    @Test
    public void testDeleteOrderItem() {
        long tableId = 1L;
        String menuItemName = "Pizza";
        int quantityToDelete = 1;

        DeleteOrderItemDTO dto = new DeleteOrderItemDTO();
        dto.setTableId(tableId);
        dto.setMenuItem(menuItemName);
        dto.setQuantity(quantityToDelete);

        Table table = new Table();
        table.setId(tableId);

        MenuItem menuItem = new MenuItem();
        menuItem.setName(menuItemName);

        MenuItemOrderStatus menuItemOrderStatus3 = new MenuItemOrderStatus(menuItem, OrderStatus.FINISHED);
        menuItem1.setId(3L);

        Order order = new Order();
        order.setMenuItems(List.of(menuItemOrderStatus1, menuItemOrderStatus2, menuItemOrderStatus3));

        List<Order> orders = List.of(order);

        when(tableRepository.findById(tableId)).thenReturn(Optional.of(table));
        when(orderRepository.findAllByTable(table)).thenReturn(orders);
        when(menuItemRepository.findByName(menuItemName)).thenReturn(Optional.of(menuItem1));

        boolean result = orderService.deleteOrderItem(dto);

        assertTrue(result);
        assertEquals(2, order.getMenuItems().size());
        verify(menuItemOrderStatusRepository, times(quantityToDelete)).deleteById(anyLong());
        verify(menuItemOrderStatusRepository, times(1))
                .deleteById(menuItemOrderStatus1.getId());
        verify(menuItemOrderStatusRepository, never())
                .deleteById(menuItemOrderStatus3.getId());
        verify(menuItemOrderStatusRepository, never())
                .deleteById(menuItemOrderStatus2.getId());
    }

    @Test
    public void testMoveOrderItem_ShouldMoveAllItemsAndMakeTheOrderFinished() {
        long fromTableId = 1L;
        long toTableId = 2L;
        String menuItemName = "Pizza";
        int quantityToMove = 2;

        MoveOrderItemDTO dto = new MoveOrderItemDTO();
        dto.setFromTableId(fromTableId);
        dto.setToTableId(toTableId);
        dto.setMenuItem(menuItemName);
        dto.setQuantity(quantityToMove);

        Table fromTable = new Table();
        fromTable.setId(fromTableId);

        Table toTable = new Table();
        toTable.setId(toTableId);

        Waiter waiter = new Waiter();
        toTable.setWaiter(waiter);

        MenuItem menuItem = new MenuItem();
        menuItem.setName(menuItemName);
        MenuItemOrderStatus menuItemOrderStatus3 = new MenuItemOrderStatus(menuItem, OrderStatus.FINISHED);
        menuItem1.setId(3L);

        Order order = new Order();
        order.setId(1);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setMenuItems(new ArrayList<>(Arrays.asList(menuItemOrderStatus1, menuItemOrderStatus3)));

        fromTable.setOrders(List.of(order));

        when(tableRepository.findById(fromTableId)).thenReturn(Optional.of(fromTable));
        when(tableRepository.findById(toTableId)).thenReturn(Optional.of(toTable));
        when(menuItemRepository.findByName(menuItemName)).thenReturn(Optional.of(menuItem1));
        when(orderRepository.saveAndFlush(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        boolean result = orderService.moveOrderItem(dto);

        assertTrue(result);
        assertEquals(OrderStatus.FINISHED, order.getOrderStatus());
        verify(orderRepository, times(1)).saveAndFlush(any(Order.class));
        verify(menuItemOrderStatusRepository, times(quantityToMove)).save(any(MenuItemOrderStatus.class));
    }

    @Test
    public void testMoveOrderItem_ShouldNotMoveOnlyPendingItem() {
        long fromTableId = 1L;
        long toTableId = 2L;
        String menuItemName = "Pizza";
        int quantityToMove = 1;

        MoveOrderItemDTO dto = new MoveOrderItemDTO();
        dto.setFromTableId(fromTableId);
        dto.setToTableId(toTableId);
        dto.setMenuItem(menuItemName);
        dto.setQuantity(quantityToMove);

        Table fromTable = new Table();
        fromTable.setId(fromTableId);

        Table toTable = new Table();
        toTable.setId(toTableId);

        Waiter waiter = new Waiter();
        toTable.setWaiter(waiter);

        MenuItem menuItem = new MenuItem();
        menuItem.setName(menuItemName);
        MenuItemOrderStatus menuItemOrderStatus3 = new MenuItemOrderStatus(menuItem, OrderStatus.FINISHED);
        menuItem1.setId(3L);

        Order order = new Order();
        order.setId(1);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setMenuItems(new ArrayList<>(Arrays.asList(menuItemOrderStatus1,menuItemOrderStatus2, menuItemOrderStatus3)));

        fromTable.setOrders(List.of(order));

        when(tableRepository.findById(fromTableId)).thenReturn(Optional.of(fromTable));
        when(tableRepository.findById(toTableId)).thenReturn(Optional.of(toTable));
        when(menuItemRepository.findByName(menuItemName)).thenReturn(Optional.of(menuItem1));
        when(orderRepository.saveAndFlush(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        boolean result = orderService.moveOrderItem(dto);

        assertTrue(result);
        assertEquals(OrderStatus.PENDING, order.getOrderStatus());

        verify(menuItemOrderStatusRepository, times(1))
                .save(menuItemOrderStatus1);
        verify(menuItemOrderStatusRepository, never())
                .save(menuItemOrderStatus2);
        verify(menuItemOrderStatusRepository, never())
                .save(menuItemOrderStatus3);
        verify(orderRepository, times(1)).saveAndFlush(any(Order.class));
        verify(menuItemOrderStatusRepository, times(quantityToMove)).save(any(MenuItemOrderStatus.class));
    }

    @Test
    public void testFinishTable_NoSuchElement() {
        when(tableRepository.findById(1L)).thenReturn(Optional.empty());
        when(waiterRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () ->
                orderService.finishTable(1L, 1L, "Card", 10.0));
    }

    @Test
    public void testFinishTable_PendingOrders() {
        Table table = new Table();
        table.setId(1L);
        table.setTableStatus(TableStatus.TAKEN);

        Waiter waiter = new Waiter();
        waiter.setId(1L);
        waiter.setTables(new ArrayList<>(Collections.singletonList(table)));

        MenuItem menuItem = new MenuItem();
        menuItem.setName("Pizza");
        menuItem.setPrice(10.0);
        menuItem.setVat(VAT.VAT20);

        MenuItemOrderStatus menuItemOrderStatus = new MenuItemOrderStatus(menuItem, OrderStatus.PENDING);
        menuItemOrderStatus.setId(1L);

        MenuItemProductQuantity menuItemProductQuantity = new MenuItemProductQuantity();
        menuItemProductQuantity.setQuantity(1.0);
        menuItemProductQuantity.setProduct(new Product());
        menuItemProductQuantity.getProduct().setName("Flour");
        menuItemProductQuantity.getProduct().setQuantityInStock(100.0);

        menuItem.setMenuItemProductsQuantity(Set.of(menuItemProductQuantity));

        Order order = new Order();
        order.setMenuItems(Collections.singletonList(menuItemOrderStatus));

        table.setOrders(new ArrayList<>(Collections.singletonList(order)));
        order.setOrderStatus(OrderStatus.PENDING);
        when(tableRepository.findById(1L)).thenReturn(Optional.of(table));
        when(waiterRepository.findById(1L)).thenReturn(Optional.of(waiter));
        when(orderRepository.findAllByTable(table)).thenReturn(Collections.singletonList(order));

        assertFalse(orderService.finishTable(1L, 1L, "Card", 10.0));
    }

    @Test
    public void testFinishTable_Success() {
        Waiter waiter = new Waiter();
        waiter.setId(1L);

        Table table = new Table();
        table.setId(1L);
        table.setTableStatus(TableStatus.TAKEN);
        table.setWaiter(waiter);

        MenuItem menuItem = new MenuItem();
        menuItem.setName("Pizza");
        menuItem.setPrice(10.0);
        menuItem.setVat(VAT.VAT20);

        MenuItemOrderStatus menuItemOrderStatus = new MenuItemOrderStatus(menuItem, OrderStatus.FINISHED);
        menuItemOrderStatus.setId(1L);

        MenuItemProductQuantity menuItemProductQuantity = new MenuItemProductQuantity();
        menuItemProductQuantity.setQuantity(1.0);
        menuItemProductQuantity.setProduct(new Product());
        menuItemProductQuantity.getProduct().setName("Flour");
        menuItemProductQuantity.getProduct().setQuantityInStock(100.0);

        menuItem.setMenuItemProductsQuantity(Set.of(menuItemProductQuantity));

        Order order = new Order();
        order.setOrderStatus(OrderStatus.FINISHED);
        order.setMenuItems(Collections.singletonList(menuItemOrderStatus));
        table.setOrders(new ArrayList<>(Collections.singletonList(order)));

        when(tableRepository.findById(1L)).thenReturn(Optional.of(table));
        when(waiterRepository.findById(1L)).thenReturn(Optional.of(waiter));
        when(orderRepository.findAllByTable(table)).thenReturn(Collections.singletonList(order));
        when(productRepository.findByName("Flour")).thenReturn(Optional.of(menuItemProductQuantity.getProduct()));

        assertTrue(orderService.finishTable(1L, 1L, "Card", 10.0));

        verify(billRepository, times(1)).saveAndFlush(any(Bill.class));
        verify(productRepository, times(1)).save(menuItemProductQuantity.getProduct());
        verify(orderRepository, times(1)).save(order);
        verify(tableRepository, times(1)).save(table);
        verify(waiterRepository, times(1)).save(waiter);
        assertEquals(99, menuItemProductQuantity.getProduct().getQuantityInStock());
        assertTrue(table.getOrders().isEmpty());
        assertEquals(table.getTableStatus(), TableStatus.AVAILABLE);
        assertNull(table.getWaiter());
    }

    @Test
    public void testOrderDone() {
        long orderId = 1;
        String category = "hotKitchen";

        Order order = new Order();
        order.setId(orderId);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setMenuItems(Arrays.asList(menuItemOrderStatus1, menuItemOrderStatus2));

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        orderService.orderDone(orderId, category);

        assertEquals(OrderStatus.FINISHED, menuItemOrderStatus1.getOrderStatus());
        assertEquals(OrderStatus.PENDING, menuItemOrderStatus2.getOrderStatus());

        assertEquals(OrderStatus.PENDING, order.getOrderStatus());

        orderService.orderDone(orderId, "coldKitchen");

        assertEquals(OrderStatus.FINISHED, order.getOrderStatus());
        verify(orderRepository, times(2)).save(order);
    }

    @Test
    public void testOrderMenuItem_Success() {
        Long menuItemId = 1L;
        int quantity = 2;
        Long tableId = 1L;

        MenuItem menuItem = new MenuItem();
        menuItem.setId(menuItemId);
        menuItem.setName("Pizza");

        Waiter waiter = new Waiter();
        waiter.setId(1L);

        Table table = new Table();
        table.setId(tableId);
        table.setWaiter(waiter);

        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(menuItem));
        when(tableRepository.findById(tableId)).thenReturn(Optional.of(table));
        when(orderRepository.saveAndFlush(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        boolean result = orderService.orderMenuItem(menuItemId, quantity, tableId);

        assertTrue(result);
        verify(orderRepository, times(1)).saveAndFlush(any(Order.class));
        verify(menuItemOrderStatusRepository, times(quantity)).save(any(MenuItemOrderStatus.class));
    }

    @Nested
    class GetPendingOrdersTest {
        private Order order;

        @BeforeEach
        public void setUp(){
            Table table = new Table();
            table.setId(1L);

            Waiter waiter = new Waiter();
            waiter.setFirstName("Pesho");

            MenuItem barMenuItem = new MenuItem();
            barMenuItem.setName("Whiskey");
            barMenuItem.setMenuItemCategory(MenuItemCategory.ALCOHOL_BEVERAGES);

            order = new Order();
            order.setId(1L);
            order.setTable(table);
            order.setWaiter(waiter);
            order.setDateAndTimeOrdered(LocalDateTime.now());
            order.setOrderStatus(OrderStatus.PENDING);
            order.setMenuItems(List.of(new MenuItemOrderStatus(barMenuItem, OrderStatus.PENDING),
                    menuItemOrderStatus1, menuItemOrderStatus2));
        }

        @Test
        public void testGetBarPendingOrders() {
            when(orderRepository.findAllByOrderStatus(OrderStatus.PENDING))
                    .thenReturn(List.of(order));
            when(tableService.getCurrentTableNumber(order.getTable().getId())).thenReturn(1);
            List<ShowOrderDto> result = orderService.getBarPendingOrders();

            assertEquals(1, result.size());
            ShowOrderDto dto = result.get(0);
            assertEquals(order.getId(), dto.getId());
            assertEquals(order.getTable().getId(), dto.getTableId());
            assertEquals(order.getWaiter().getFirstName(), dto.getWaiterFirstName());
            assertEquals(order.getDateAndTimeOrdered(), dto.getDateAndTimeOrdered());
            assertEquals(1, dto.getMenuItems().size());

            ShowOrderMenuItemDto menuItemDto = dto.getMenuItems().get(0);
            assertEquals("Whiskey", menuItemDto.getMenuItemName());
            assertEquals(1, menuItemDto.getQuantity());
            assertEquals(menuItemOrderStatus1.getComment(), menuItemDto.getComment());

            verify(orderRepository, times(1)).findAllByOrderStatus(OrderStatus.PENDING);
        }

        @Test
        public void test_Get_Hot_Kitchen_PendingOrders() {
            when(orderRepository.findAllByOrderStatus(OrderStatus.PENDING))
                    .thenReturn(List.of(order));
            when(tableService.getCurrentTableNumber(order.getTable().getId())).thenReturn(1);
            List<ShowOrderDto> result = orderService.getHotKitchenPendingOrders();

            assertEquals(1, result.size());
            ShowOrderDto dto = result.get(0);
            assertEquals(order.getId(), dto.getId());
            assertEquals(order.getTable().getId(), dto.getTableId());
            assertEquals(order.getWaiter().getFirstName(), dto.getWaiterFirstName());
            assertEquals(order.getDateAndTimeOrdered(), dto.getDateAndTimeOrdered());
            assertEquals(1, dto.getMenuItems().size());

            ShowOrderMenuItemDto menuItemDto = dto.getMenuItems().get(0);
            assertEquals(menuItem1.getName(), menuItemDto.getMenuItemName());
            assertEquals(1, menuItemDto.getQuantity());
            assertEquals(menuItemOrderStatus1.getComment(), menuItemDto.getComment());

            verify(orderRepository, times(1)).findAllByOrderStatus(OrderStatus.PENDING);
        }

        @Test
        public void test_Get_Cold_Kitchen_PendingOrders() {
            when(orderRepository.findAllByOrderStatus(OrderStatus.PENDING))
                    .thenReturn(List.of(order));
            when(tableService.getCurrentTableNumber(order.getTable().getId())).thenReturn(1);
            List<ShowOrderDto> result = orderService.getColdKitchenPendingOrders();

            assertEquals(1, result.size());
            ShowOrderDto dto = result.get(0);
            assertEquals(order.getId(), dto.getId());
            assertEquals(order.getTable().getId(), dto.getTableId());
            assertEquals(order.getWaiter().getFirstName(), dto.getWaiterFirstName());
            assertEquals(order.getDateAndTimeOrdered(), dto.getDateAndTimeOrdered());
            assertEquals(1, dto.getMenuItems().size());

            ShowOrderMenuItemDto menuItemDto = dto.getMenuItems().get(0);
            assertEquals(menuItem2.getName(), menuItemDto.getMenuItemName());
            assertEquals(1, menuItemDto.getQuantity());
            assertEquals(menuItemOrderStatus1.getComment(), menuItemDto.getComment());

            verify(orderRepository, times(1)).findAllByOrderStatus(OrderStatus.PENDING);
        }

    }



}
