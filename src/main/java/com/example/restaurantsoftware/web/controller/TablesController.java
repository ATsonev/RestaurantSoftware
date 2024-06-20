package com.example.restaurantsoftware.web.controller;

import com.example.restaurantsoftware.model.Waiter;
import com.example.restaurantsoftware.model.base.BaseEntity;
import com.example.restaurantsoftware.model.dto.ShowWaiterTablesDTO;
import com.example.restaurantsoftware.service.TableService;
import com.example.restaurantsoftware.service.WaiterService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TablesController {
    private final WaiterService waiterService;
    private final TableService tableService;
    private final ModelMapper modelMapper;

    public TablesController(WaiterService waiterService, TableService tableService, ModelMapper modelMapper) {
        this.waiterService = waiterService;
        this.tableService = tableService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/tables")
    public String tables(Model model, HttpSession session){
        Long waiterId = (Long) session.getAttribute("waiterId");
        Waiter waiter = waiterService.findWaiterByID(waiterId);
        ShowWaiterTablesDTO dto = modelMapper.map(waiter, ShowWaiterTablesDTO.class);
        List<Long> waiterTablesIds = waiter.getTables().stream().map(BaseEntity::getId).collect(Collectors.toList());
        dto.setTableIds(waiterTablesIds);

        model.addAttribute("tables", tableService.getAllTables());
        model.addAttribute("waiters", waiterService.showWaiters());
        model.addAttribute("waiterDto", dto);
        return "tables";
    }

    @PostMapping("/setWaiter")
    public String setWaiter(@RequestParam("tableId") Long tableId,
                            @RequestParam("waiterId") Long waiterId) {
        tableService.setWaiter(tableId, waiterId);
        return "redirect:/tables";
    }

}
