package com.example.vehiclesystem.controller;

import com.example.apiconnector.dto.VehicleDTO;
import com.example.vehiclesystem.entity.Vehicle;
import com.example.vehiclesystem.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService service;

    public VehicleController(VehicleService service) {
        this.service = service;
    }

    // Get all vehicles
    @GetMapping
    public List<VehicleDTO> getAllVehicles() {
        return service.getAllVehicles().stream()
                .map(v -> new VehicleDTO(v.getId(), v.getName(), v.getPricePerDay()))
                .collect(Collectors.toList());
    }

    // Get vehicle by ID (with 404 handling)
    @GetMapping("/{id}")
    public ResponseEntity<VehicleDTO> getVehicle(@PathVariable("id") Long id) {
        return service.getVehicleByIdOptional(id)
                .map(v -> ResponseEntity.ok(new VehicleDTO(v.getId(), v.getName(), v.getPricePerDay())))
                .orElse(ResponseEntity.notFound().build());
    }

    // Add a new vehicle
    @PostMapping
    public ResponseEntity<VehicleDTO> addVehicle(@Valid @RequestBody Vehicle vehicle) {
        // Prevent adding duplicate vehicle names
        if (service.vehicleExists(vehicle.getName())) {
            return ResponseEntity.badRequest()
                    .body(new VehicleDTO(null, "Vehicle with this name already exists", vehicle.getPricePerDay()));
        }

        Vehicle saved = service.addVehicle(vehicle);
        VehicleDTO dto = new VehicleDTO(saved.getId(), saved.getName(), saved.getPricePerDay());
        return ResponseEntity.ok(dto);
    }

    // Delete vehicle by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable("id") Long id) {
        boolean deleted = service.deleteVehicle(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
