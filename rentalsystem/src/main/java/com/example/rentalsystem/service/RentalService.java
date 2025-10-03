package com.example.rentalsystem.service;

import com.example.apiconnector.dto.VehicleDTO;
import com.example.rentalsystem.entity.Rental;
import com.example.apiconnector.enums.RentalStatus;
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

    public Rental addRental(Rental rental) {
        rental.setStatus(RentalStatus.BOOKED); 
        return repository.save(rental);
    }

    public List<Rental> getAllRentals() {
        List<Rental> rentals = repository.findAll();

       
        rentals.forEach(r -> {
            if (r.getStatus() == RentalStatus.BOOKED && r.getEndDate().isBefore(LocalDate.now())) {
                r.setStatus(RentalStatus.COMPLETED);
                repository.save(r);
            }
        });

        return rentals;
    }

    public void deleteRental(Long id) {
        repository.deleteById(id);
    }

    public Rental cancelRental(Long id) {
        Rental rental = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rental not found"));

        if (rental.getStatus() != RentalStatus.BOOKED) {
            throw new RuntimeException("Only booked rentals can be cancelled");
        }

        rental.setStatus(RentalStatus.CANCELLED);
        return repository.save(rental);
    }

    public Rental createRental(String customerName, VehicleDTO vehicle, LocalDate startDate, LocalDate endDate) {
       
        boolean overlapping = repository.findAll().stream()
                .anyMatch(r -> r.getVehicleId().equals(vehicle.getId()) &&
                        r.getStatus() == RentalStatus.BOOKED &&
                        !(endDate.isBefore(r.getStartDate()) || startDate.isAfter(r.getEndDate())));

        if (overlapping) {
            throw new RuntimeException("Vehicle is already booked in that period");
        }

        Rental rental = new Rental();
        rental.setCustomerName(customerName);
        rental.setVehicleId(vehicle.getId());
        rental.setVehicleModel(vehicle.getModel());
        rental.setStartDate(startDate);
        rental.setEndDate(endDate);
        rental.setPrice(vehicle.getPricePerDay() * (endDate.toEpochDay() - startDate.toEpochDay()+1));
        rental.setStatus(RentalStatus.BOOKED);

        return repository.save(rental);
    }
}
