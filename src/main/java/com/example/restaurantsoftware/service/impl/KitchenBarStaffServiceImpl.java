package com.example.restaurantsoftware.service.impl;

import com.example.restaurantsoftware.model.dto.staffDto.AddKitchenBarStaffDTO;
import com.example.restaurantsoftware.model.dto.staffDto.KitchenBarStaffDto;
import com.example.restaurantsoftware.service.KitchenBarStaffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(KitchenBarStaffServiceImpl.class);
    private final RestTemplate restTemplate;
    public KitchenBarStaffServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Optional<KitchenBarStaffDto> findByPassword(String password) {
        KitchenBarStaffDto requestDto = new KitchenBarStaffDto();
        requestDto.setPassword(password);

        try {
            logger.debug("Sending request to find staff by password");
            ResponseEntity<KitchenBarStaffDto> responseEntity = restTemplate.exchange(
                    "http://localhost:8081/kitchen-bar-staff/by-password",
                    HttpMethod.POST,
                    new HttpEntity<>(requestDto),
                    KitchenBarStaffDto.class
            );

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                logger.debug("Received response: {}", responseEntity.getBody());
                return Optional.ofNullable(responseEntity.getBody());
            } else {
                logger.warn("Received non-OK response: {}", responseEntity.getStatusCode());
                return Optional.empty();
            }
        } catch (HttpClientErrorException e) {
            logger.error("HTTP error occurred: {}", e.getStatusCode(), e);
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
