package com.example.restaurantsoftware.service.impl;

import com.example.restaurantsoftware.model.dto.staffDto.AddKitchenBarStaffDTO;
import com.example.restaurantsoftware.model.dto.staffDto.KitchenBarStaffDto;
import com.example.restaurantsoftware.service.KitchenBarStaffService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class KitchenBarStaffServiceImpl implements KitchenBarStaffService {

    private final RestTemplate restTemplate;
    public KitchenBarStaffServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Optional<KitchenBarStaffDto> findByPassword(String password) {
        KitchenBarStaffDto requestDto = new KitchenBarStaffDto();
        requestDto.setPassword(password);

        try {
            ResponseEntity<KitchenBarStaffDto> responseEntity = restTemplate.exchange(
                    "http://localhost:8081/kitchen-bar-staff/by-password",
                    HttpMethod.POST,
                    new HttpEntity<>(requestDto),
                    KitchenBarStaffDto.class
            );

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return Optional.ofNullable(responseEntity.getBody());
            } else {
                return Optional.empty();
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return Optional.empty();
            } else {
                throw e;
            }
        }
    }
    @Override
    public void addStaff(AddKitchenBarStaffDTO dto) {
        restTemplate.postForObject("http://localhost:8081/kitchen-bar-staff", dto, KitchenBarStaffDto.class);
    }

    @Override
    public List<KitchenBarStaffDto> getAllStaff() {
        ResponseEntity<KitchenBarStaffDto[]> response = restTemplate.getForEntity("http://localhost:8081/kitchen-bar-staff",
                KitchenBarStaffDto[].class);
        KitchenBarStaffDto[] staffArray = response.getBody();
        if(staffArray != null){
            return Arrays.asList(staffArray);
        }
        return List.of();
    }

    @Override
    public void deleteAccount(long id) {
        String url = "http://localhost:8081/kitchen-bar-staff/delete-account/" + id;
        restTemplate.delete(url);
    }

}
