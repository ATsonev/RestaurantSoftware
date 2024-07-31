package com.example.restaurantsoftware.service.impl;

import com.example.restaurantsoftware.model.dto.staffDto.AddKitchenBarStaffDTO;
import com.example.restaurantsoftware.model.dto.staffDto.KitchenBarStaffDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class KitchenBarStaffServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private KitchenBarStaffServiceImpl kitchenBarStaffService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByPassword_Success() {
        String password = "testPassword";
        KitchenBarStaffDto responseDto = new KitchenBarStaffDto();
        responseDto.setPassword(password);

        ResponseEntity<KitchenBarStaffDto> responseEntity = new ResponseEntity<>(responseDto, HttpStatus.OK);
        when(restTemplate.exchange(
                eq("http://localhost:8081/kitchen-bar-staff/by-password"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(KitchenBarStaffDto.class)
        )).thenReturn(responseEntity);

        Optional<KitchenBarStaffDto> result = kitchenBarStaffService.findByPassword(password);

        assert(result.isPresent());
        assert(result.get().getPassword().equals(password));
    }

    @Test
    public void testFindByPassword_NotFound() {

        String password = "testPassword";

        when(restTemplate.exchange(
                eq("http://localhost:8081/kitchen-bar-staff/by-password"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(KitchenBarStaffDto.class)
        )).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        Optional<KitchenBarStaffDto> result = kitchenBarStaffService.findByPassword(password);

        assert(result.isEmpty());
    }

    @Test
    public void testAddStaff() {
        AddKitchenBarStaffDTO dto = new AddKitchenBarStaffDTO();
        KitchenBarStaffDto responseDto = new KitchenBarStaffDto();
        when(restTemplate.postForObject(
                eq("http://localhost:8081/kitchen-bar-staff"),
                eq(dto),
                eq(KitchenBarStaffDto.class)
        )).thenReturn(responseDto);

        kitchenBarStaffService.addStaff(dto);

        verify(restTemplate).postForObject("http://localhost:8081/kitchen-bar-staff", dto, KitchenBarStaffDto.class);
    }

    @Test
    public void testGetAllStaff_Success() {
        KitchenBarStaffDto[] staffArray = { new KitchenBarStaffDto(), new KitchenBarStaffDto() };
        ResponseEntity<KitchenBarStaffDto[]> responseEntity = new ResponseEntity<>(staffArray, HttpStatus.OK);
        when(restTemplate.getForEntity(
                eq("http://localhost:8081/kitchen-bar-staff"),
                eq(KitchenBarStaffDto[].class)
        )).thenReturn(responseEntity);

        List<KitchenBarStaffDto> result = kitchenBarStaffService.getAllStaff();

        assert(result.size() == 2);
    }

}
