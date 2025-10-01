
package com.example.rentalsystem.console;

import com.example.apiconnector.dto.VehicleDTO;
import com.example.rentalsystem.entity.Rental;
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
            System.out.println("4. Exit");
            System.out.print("Choose option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1: {
                    try {
                        System.out.print("Enter customer name: ");
                        String customer = scanner.nextLine();

                        System.out.print("Enter vehicle ID: ");
                        Long vehicleId = Long.parseLong(scanner.nextLine());

                        VehicleDTO vehicle = vehicleService.getVehicleById(vehicleId);
                        if (vehicle == null) {
                            System.out.println("Vehicle not found!");
                            break;
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
                    break;
                }
                case 2: {
                    List<Rental> rentals = rentalService.getAllRentals();
                    if (rentals.isEmpty()) {
                        System.out.println("No rentals found.");
                    } else {
                        rentals.forEach(r -> System.out.println(r.getId() + " - " +
                                r.getCustomerName() + " rented " + r.getVehicleModel() +
                                " from " + r.getStartDate() + " to " + r.getEndDate() +
                                " ($" + r.getPrice() + ")"));
                    }
                    break;
                }
                case 3: {
                    System.out.print("Enter rental ID to delete: ");
                    Long rentalId = Long.parseLong(scanner.nextLine());
                    rentalService.deleteRental(rentalId);
                    System.out.println("Rental deleted successfully!");
                    break;
                }
                case 4: {
                    System.out.println("Exiting...");
                    return;
                }
                default: 
                    System.out.println("Invalid option, try again.");
            }
        }
    }
}
