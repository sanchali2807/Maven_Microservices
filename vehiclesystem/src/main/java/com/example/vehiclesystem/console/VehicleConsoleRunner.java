package com.example.vehiclesystem.console;

import com.example.vehiclesystem.entity.Vehicle;
import com.example.vehiclesystem.service.VehicleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class VehicleConsoleRunner implements CommandLineRunner {

    private final VehicleService service;

    public VehicleConsoleRunner(VehicleService service) {
        this.service = service;
    }

 @Override
public void run(String... args) throws Exception {
    Scanner scanner = new Scanner(System.in);

   while (true) {
    System.out.println("\n=== Vehicle Management ===");
    System.out.println("1. Add Vehicle");
    System.out.println("2. List Vehicles");
    System.out.println("3. Delete Vehicle");
    System.out.println("4. Exit");
    System.out.print("Enter your choice: ");
    int choice = scanner.nextInt();
    scanner.nextLine(); // consume newline

    if (choice == 1) {
        addVehicle(scanner);
    } else if (choice == 2) {
        listVehicles();
    } else if (choice == 3) {
        deleteVehicle(scanner);
    } else if (choice == 4) {
        System.out.println("Exiting application. Goodbye!");
        break;
    } else {
        System.out.println("Invalid choice! Please try again.");
    }
}

}


    private void addVehicle(Scanner scanner) {
        System.out.print("Enter vehicle name: ");
        String name = scanner.nextLine();

        // Check for duplicate
        if (service.vehicleExists(name)) {
            System.out.println("A vehicle with this name is already registered!");
            return;
        }

        System.out.print("Enter vehicle type: ");
        String type = scanner.nextLine();

        System.out.print("Enter price per day: ");
        double pricePerDay;

        try {
            pricePerDay = Double.parseDouble(scanner.nextLine());
            if (pricePerDay <= 0) {
                System.out.println("Price must be positive!");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid price input!");
            return;
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setName(name);
        vehicle.setType(type);
        vehicle.setPricePerDay(pricePerDay);

        service.addVehicle(vehicle);
        System.out.println("Vehicle added successfully!");
    }

    private void listVehicles() {
        List<Vehicle> vehicles = service.getAllVehicles();

        if (vehicles.isEmpty()) {
            System.out.println("No vehicles found.");
            return;
        }

        System.out.println("\n--- Vehicle List ---");
        vehicles.forEach(v -> System.out.println(
                v.getId() + " - " + v.getName() + " (" + v.getType() + ") - Price: " + v.getPricePerDay()
        ));
    }

    private void deleteVehicle(Scanner scanner) {
        System.out.print("Enter vehicle ID to delete: ");
        long id;

        try {
            id = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID input!");
            return;
        }

        if (service.deleteVehicle(id)) {
            System.out.println("Vehicle deleted successfully!");
        } else {
            System.out.println("Vehicle not found with ID: " + id);
        }
    }
}
