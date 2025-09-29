package com.example.rentalsystem.service;

import com.example.apiconnector.dto.VehicleDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VehicleClientService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String vehicleServiceUrl = "http://localhost:8081/vehicles";

    public VehicleDTO getVehicleById(Long id) {
        return restTemplate.getForObject(vehicleServiceUrl + "/" + id, VehicleDTO.class);
    }
}
