
package com.example.rentalsystem.service;

import com.example.apiconnector.dto.VehicleDTO;
import com.example.rentalsystem.entity.Rental;
import com.example.rentalsystem.repository.RentalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RentalService {

    private final RentalRepository repository;

    public RentalService(RentalRepository repository) {
        this.repository = repository;
    }

    public Rental createRental(String customerName, VehicleDTO vehicle, LocalDate startDate, LocalDate endDate) {
        if (isVehicleRented(vehicle.getId(), startDate, endDate)) {
            throw new IllegalStateException("Vehicle is already rented in that period.");
        }

        Rental rental = new Rental();
        rental.setCustomerName(customerName);
        rental.setVehicleId(vehicle.getId());
        rental.setVehicleModel(vehicle.getModel());
        rental.setStartDate(startDate);
        rental.setEndDate(endDate);
        rental.setPrice(vehicle.getPricePerDay() * (endDate.toEpochDay() - startDate.toEpochDay()));

        return repository.save(rental);
    }

    private boolean isVehicleRented(Long vehicleId, LocalDate start, LocalDate end) {
        return repository.findAll().stream()
                .anyMatch(r -> r.getVehicleId().equals(vehicleId) &&
                        !(end.isBefore(r.getStartDate()) || start.isAfter(r.getEndDate())));
    }

    public List<Rental> getAllRentals() {
        return repository.findAll();
    }

    public void deleteRental(Long id) {
        repository.deleteById(id);
    }
}



