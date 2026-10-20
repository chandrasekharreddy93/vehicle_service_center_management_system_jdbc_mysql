package com.stackly.vehicleservicecentermanagementsystem_jdbc_mysql.project;

import java.sql.Connection;
import java.util.Scanner;

public class VehicleServiceCenterMain {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        try (Connection con = DBConnection.getConnection()) {

            System.out.println("Database connected successfully.");
            TableCreation.createTables(con);

            int choice = -1;

            do {
                System.out.println("\n===== VEHICLE SERVICE CENTER =====");
                System.out.println("1. Register Customer");
                System.out.println("2. View Customers");
                System.out.println("3. Register Vehicle");
                System.out.println("4. View Vehicles");
                System.out.println("5. Book Service");
                System.out.println("6. View Service History");
                System.out.println("7. Search Vehicle");
                System.out.println("8. Search Service History");
                System.out.println("0. Exit");
                System.out.print("Enter your choice: ");

                try {
                    choice = Integer.parseInt(sc.nextLine().trim());

                    switch (choice) {
                        case 1:
                            VehicleServiceOperations.addCustomer(con, sc);
                            break;
                        case 2:
                            VehicleServiceOperations.viewCustomers(con);
                            break;
                        case 3:
                            VehicleServiceOperations.addVehicle(con, sc);
                            break;
                        case 4:
                            VehicleServiceOperations.viewVehicles(con);
                            break;
                        case 5:
                            VehicleServiceOperations.bookService(con, sc);
                            break;
                        case 6:
                            VehicleServiceOperations.viewServiceHistory(con);
                            break;
                        case 7:
                            VehicleServiceOperations.searchVehicle(con, sc);
                            break;
                        case 8:
                            VehicleServiceOperations.searchServiceHistory(con, sc);
                            break;
                        case 0:
                            System.out.println("Thank you for using the system.");
                            break;
                        default:
                            System.out.println("Invalid choice. Try again.");
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                } catch (Exception e) {
                    System.out.println("Operation failed: " + e.getMessage());
                }

            } while (choice != 0);

        } catch (Exception e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }

        sc.close();
    }
}
