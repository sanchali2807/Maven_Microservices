package com.example.rentalsystem.service;

import com.example.rentalsystem.entity.Rental;
import com.example.rentalsystem.repository.RentalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentalService {

    private final RentalRepository repository;

    public RentalService(RentalRepository repository) {
        this.repository = repository;
    }

    public Rental addRental(Rental rental) {
        return repository.save(rental);
    }

    public List<Rental> getAllRentals() {
        return repository.findAll();
    }

    // Add this method for deleting a rental by ID
    public void deleteRental(Long id) {
        repository.deleteById(id);
    }
}
