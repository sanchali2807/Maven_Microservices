package com.example.vehiclesystem.repository;

import com.example.vehiclesystem.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
     boolean existsByName(String name);
}
