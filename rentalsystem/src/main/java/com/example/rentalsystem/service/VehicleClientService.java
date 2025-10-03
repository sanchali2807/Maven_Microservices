package com.example.rentalsystem.service;

import com.example.apiconnector.dto.VehicleDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class VehicleClientService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String vehicleServiceUrl = "http://localhost:8081/vehicles";

  
    public VehicleDTO getVehicleById(Long id) {
        return restTemplate.getForObject(vehicleServiceUrl + "/" + id, VehicleDTO.class);
    }

    
    public List<VehicleDTO> getAllVehicles() {
        VehicleDTO[] vehicles = restTemplate.getForObject(vehicleServiceUrl, VehicleDTO[].class);
        if (vehicles != null) {
            return Arrays.asList(vehicles);
        } else {
            return List.of();
        }
    }
}
