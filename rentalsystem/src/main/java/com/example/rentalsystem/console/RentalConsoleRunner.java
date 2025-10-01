package com.example.rentalsystem.console;

import com.example.apiconnector.dto.VehicleDTO;
import com.example.rentalsystem.entity.Rental;
// import com.example.apiconnector.enums.RentalStatus;
import com.example.rentalsystem.service.RentalService;
import com.example.rentalsystem.service.VehicleClientService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

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
        Scanner scanner = new Scanner(System.in);

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

    private void addRental(Scanner scanner) {
        try {
            System.out.print("Enter customer name: ");
            String customer = scanner.nextLine();

            System.out.print("Enter vehicle ID: ");
            Long vehicleId = Long.parseLong(scanner.nextLine());

            VehicleDTO vehicle = vehicleService.getVehicleById(vehicleId);
            if (vehicle == null) {
                System.out.println("Vehicle not found!");
                return;
            }

            System.out.print("Enter start date (yyyy-mm-dd): ");
            LocalDate startDate = LocalDate.parse(scanner.nextLine());

            System.out.print("Enter end date (yyyy-mm-dd): ");
            LocalDate endDate = LocalDate.parse(scanner.nextLine());

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

    private void deleteRental(Scanner scanner) {
        System.out.print("Enter rental ID to delete: ");
        try {
            Long rentalId = Long.parseLong(scanner.nextLine());
            rentalService.deleteRental(rentalId);
            System.out.println("Rental deleted successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void cancelRental(Scanner scanner) {
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
