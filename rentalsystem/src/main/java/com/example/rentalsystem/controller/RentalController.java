package com.example.rentalsystem.controller;

import com.example.apiconnector.dto.VehicleDTO;
import com.example.rentalsystem.entity.Rental;
import com.example.rentalsystem.service.RentalService;
import com.example.rentalsystem.service.VehicleClientService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rentals")
public class RentalController {

    private final RentalService rentalService;
    private final VehicleClientService vehicleClientService;

    public RentalController(RentalService rentalService, VehicleClientService vehicleClientService) {
        this.rentalService = rentalService;
        this.vehicleClientService = vehicleClientService;
    }

    @GetMapping
    public List<Rental> getAllRentals() {
        return rentalService.getAllRentals();
    }

    @PostMapping
    public Rental addRental(@Valid @RequestBody Rental rental) {
        // Check if vehicle exists
        VehicleDTO vehicle = vehicleClientService.getVehicleById(rental.getVehicleId());
        if (vehicle == null) {
            throw new RuntimeException("Vehicle not found for rental");
        }
        return rentalService.addRental(rental);
    }
}
