package com.example.rentalsystem.console;

import com.example.apiconnector.dto.VehicleDTO;
import com.example.rentalsystem.entity.Rental;
import com.example.apiconnector.enums.RentalStatus;
import com.example.rentalsystem.service.RentalService;
import com.example.rentalsystem.service.VehicleClientService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class RentalConsoleRunner implements CommandLineRunner {

    private final RentalService rentalService;
    private final VehicleClientService vehicleService;

    public RentalConsoleRunner(RentalService rentalService, VehicleClientService vehicleService) {
        this.rentalService = rentalService;
        this.vehicleService = vehicleService;
    }

    @Override
    public void run(String... args) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);

        while (true) {
            System.out.println("\n=== Rental Management ===");
            System.out.println("1. Add Rental");
            System.out.println("2. List Rentals");
            System.out.println("3. Delete Rental");
            System.out.println("4. Cancel Rental");
            System.out.println("5. Exit");
            System.out.print("Choose option: ");

            String input = scanner.nextLine();
            int choice;

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a number between 1-5.");
                continue;
            }

            switch (choice) {
                case 1:
                    addRental(scanner);
                    break;
                case 2:
                    listRentals();
                    break;
                case 3:
                    deleteRental(scanner);
                    break;
                case 4:
                    cancelRental(scanner);
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option, try again.");
            }
        }
    }

    private void addRental(java.util.Scanner scanner) {
        try {
            List<VehicleDTO> vehicles = vehicleService.getAllVehicles();
            if (vehicles.isEmpty()) {
                System.out.println("No vehicles available.");
                return;
            }
            System.out.println("Available Vehicles:");
            vehicles.forEach(v -> System.out.println(v.getId() + " - " + v.getModel() + " ($" + v.getPricePerDay() + " per day)"));

            System.out.print("Enter customer name: ");
            String customer = scanner.nextLine();
            if (!customer.matches("[a-zA-Z ]+")) {
    System.out.println("Error: Customer name must contain letters only.");
    return;
}

            System.out.print("Enter vehicle ID: ");
            Long vehicleId = Long.parseLong(scanner.nextLine());

            VehicleDTO vehicle = vehicleService.getVehicleById(vehicleId);
            if (vehicle == null) {
                System.out.println("Vehicle not found!");
                return;
            }

            System.out.print("Enter start date (yyyy-mm-dd): ");
            LocalDate startDate = LocalDate.parse(scanner.nextLine());
            if (startDate.isBefore(LocalDate.now())) {
                System.out.println("Error: Start date cannot be in the past.");
                return;
            }

            System.out.print("Enter end date (yyyy-mm-dd): ");
            LocalDate endDate = LocalDate.parse(scanner.nextLine());
            if (endDate.isBefore(startDate)) {
                System.out.println("Error: End date cannot be before start date.");
                return;
            }

            Rental rental = rentalService.createRental(customer, vehicle, startDate, endDate);
            System.out.println("Rental added successfully! ID: " + rental.getId());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void listRentals() {
        List<Rental> rentals = rentalService.getAllRentals();
        if (rentals.isEmpty()) {
            System.out.println("No rentals found.");
        } else {
            rentals.forEach(r -> System.out.println(r.getId() + " - " +
                    r.getCustomerName() + " rented " + r.getVehicleModel() +
                    " from " + r.getStartDate() + " to " + r.getEndDate() +
                    " ($" + r.getPrice() + ") Status: " + r.getStatus()));
        }
    }

    private void deleteRental(java.util.Scanner scanner) {
        System.out.print("Enter rental ID to delete: ");
        try {
            Long rentalId = Long.parseLong(scanner.nextLine());

            if (rentalId <= 0) {
                System.out.println("Error: Rental ID must be positive.");
                return;
            }

            boolean exists = rentalService.getAllRentals().stream()
                    .anyMatch(r -> r.getId().equals(rentalId));

            if (!exists) {
                System.out.println("Error: Rental with ID " + rentalId + " does not exist.");
                return;
            }

            rentalService.deleteRental(rentalId);
            System.out.println("Rental deleted successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid rental ID. Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void cancelRental(java.util.Scanner scanner) {
    
        List<Rental> bookedRentals = rentalService.getAllRentals().stream()
                .filter(r -> r.getStatus() == RentalStatus.BOOKED)
                .toList();

        if (bookedRentals.isEmpty()) {
            System.out.println("No booked rentals available to cancel.");
            return;
        }

        System.out.println("Booked Rentals:");
        bookedRentals.forEach(r -> System.out.println(r.getId() + " - " +
                r.getCustomerName() + " rented " + r.getVehicleModel() +
                " from " + r.getStartDate() + " to " + r.getEndDate() +
                " ($" + r.getPrice() + ")"));

        System.out.print("Enter rental ID to cancel: ");
        try {
            Long rentalId = Long.parseLong(scanner.nextLine());
            Rental cancelled = rentalService.cancelRental(rentalId);
            System.out.println("Rental cancelled successfully! Status: " + cancelled.getStatus());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
