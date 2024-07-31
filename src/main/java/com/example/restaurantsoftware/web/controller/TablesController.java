package com.example.restaurantsoftware.web.controller;

import com.example.restaurantsoftware.model.Waiter;
import com.example.restaurantsoftware.model.base.BaseEntity;
import com.example.restaurantsoftware.model.dto.waiterDto.ShowWaiterTablesDTO;
import com.example.restaurantsoftware.model.user.CurrentUserDetails;
import com.example.restaurantsoftware.service.TableService;
import com.example.restaurantsoftware.service.WaiterService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String tables(Model model, @AuthenticationPrincipal CurrentUserDetails currentUserDetails){
        Long waiterId = currentUserDetails.getId();
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

    @PostMapping("/add-table")
    public String addTable(@RequestParam("capacity")int capacity){
        tableService.addTable(capacity);
        return "redirect:/tables";
    }

    @PostMapping("/delete-table")
    public String deleteTable(@RequestParam("tableId") Long tableId,  RedirectAttributes redirectAttributes) {
        boolean result = tableService.deleteTable(tableId);
        if(!result){
            redirectAttributes.addFlashAttribute("deleteError", "You cannot delete a non-empty table.");
        }
        return "redirect:/tables";
    }

}
