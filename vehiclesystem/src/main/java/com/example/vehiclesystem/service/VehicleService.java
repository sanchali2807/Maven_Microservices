package com.example.vehiclesystem.service;

import com.example.vehiclesystem.entity.Vehicle;
import com.example.vehiclesystem.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    private final VehicleRepository repository;

    public VehicleService(VehicleRepository repository) {
        this.repository = repository;
    }

    // Get all vehicles
    public List<Vehicle> getAllVehicles() {
        return repository.findAll();
    }

    // Old style: throws if not found
    public Vehicle getVehicleById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id " + id));
    }

    // New style: safe lookup with Optional
    public Optional<Vehicle> getVehicleByIdOptional(Long id) {
        return repository.findById(id);
    }

    // Add vehicle
    public Vehicle addVehicle(Vehicle vehicle) {
        return repository.save(vehicle);
    }

    // Check if vehicle with given name exists
    public boolean vehicleExists(String name) {
        return repository.existsByName(name);
    }

    // Delete vehicle by ID safely
    public boolean deleteVehicle(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
