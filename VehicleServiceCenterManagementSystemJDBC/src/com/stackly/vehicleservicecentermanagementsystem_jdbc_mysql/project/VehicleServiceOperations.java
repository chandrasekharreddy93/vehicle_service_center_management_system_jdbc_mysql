package com.stackly.vehicleservicecentermanagementsystem_jdbc_mysql.project;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class VehicleServiceOperations {

    // 1. Register customer
    public static void addCustomer(Connection con, Scanner sc) throws Exception {

        System.out.print("Enter customer name: ");
        String name = sc.nextLine().trim();

        if (!Validation.isValidName(name)) {
            System.out.println("Invalid customer name.");
            return;
        }

        System.out.print("Enter email: ");
        String email = sc.nextLine().trim();

        if (!Validation.isValidEmail(email)) {
            System.out.println("Invalid email.");
            return;
        }

        System.out.print("Enter phone number: ");
        String phone = sc.nextLine().trim();

        if (!Validation.isValidPhone(phone)) {
            System.out.println("Invalid phone number.");
            return;
        }

        String sql = "INSERT INTO customers(name, email, phone) VALUES (?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.executeUpdate();
            System.out.println("Customer registered successfully.");
        } catch (java.sql.SQLException e) {
            if (e.getErrorCode() == 1062) {
                System.out.println("Email already registered.");
            } else {
                throw e;
            }
        }
    }

    // 2. View customers
    public static void viewCustomers(Connection con) throws Exception {

        String sql = "SELECT customer_id, name, email, phone FROM customers ORDER BY customer_id";

        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\nCUSTOMER DETAILS");
            System.out.printf("%-12s %-22s %-30s %-15s%n", "Customer ID", "Name", "Email", "Phone");
            System.out.println("----------------------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-12d %-22s %-30s %-15s%n", rs.getInt("customer_id"), rs.getString("name"), rs.getString("email"), rs.getString("phone"));
            }
        }
    }

    // 3. Register vehicle
    public static void addVehicle(Connection con, Scanner sc) throws Exception {

        System.out.print("Enter customer ID: ");
        int customerId;

        try {
            customerId = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid customer ID.");
            return;
        }

        System.out.print("Enter vehicle number: ");
        String vehicleNumber = sc.nextLine().trim().toUpperCase();

        if (!Validation.isValidVehicleNumber(vehicleNumber)) {
            System.out.println("Invalid vehicle number.");
            return;
        }

        System.out.print("Enter vehicle model: ");
        String model = sc.nextLine().trim();

        if (!Validation.isValidModel(model)) {
            System.out.println("Invalid vehicle model.");
            return;
        }

        String sql = "INSERT INTO vehicles(customer_id, vehicle_number, model) VALUES (?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ps.setString(2, vehicleNumber);
            ps.setString(3, model);
            ps.executeUpdate();
            System.out.println("Vehicle registered successfully.");
        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            System.out.println("Customer ID does not exist or vehicle number is already registered.");
        }
    }

    // 4. View vehicles using JOIN
    public static void viewVehicles(Connection con) throws Exception {

        String sql = "SELECT v.vehicle_id, c.name, v.vehicle_number, v.model FROM vehicles v JOIN customers c ON v.customer_id = c.customer_id ORDER BY v.vehicle_id";

        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\nREGISTERED VEHICLES");
            System.out.printf("%-12s %-22s %-20s %-22s%n", "Vehicle ID", "Customer Name", "Vehicle Number", "Model");
            System.out.println("------------------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-12d %-22s %-20s %-22s%n", rs.getInt("vehicle_id"), rs.getString("name"), rs.getString("vehicle_number"), rs.getString("model"));
            }
        }
    }

    // 5. Book service
    public static void bookService(Connection con, Scanner sc) throws Exception {

        System.out.print("Enter vehicle ID: ");
        int vehicleId;

        try {
            vehicleId = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid vehicle ID.");
            return;
        }

        System.out.print("Enter service date (YYYY-MM-DD): ");
        String dateInput = sc.nextLine().trim();
        Date serviceDate;

        try {
            serviceDate = Date.valueOf(dateInput);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date. Use YYYY-MM-DD.");
            return;
        }

        if (serviceDate.before(new Date(System.currentTimeMillis()))) {
            System.out.println("Service date cannot be in the past.");
            return;
        }

        System.out.print("Enter service description: ");
        String description = sc.nextLine().trim();

        if (!Validation.isValidDescription(description)) {
            System.out.println("Description cannot be empty.");
            return;
        }

        System.out.print("Enter estimated service cost: ");
        double cost;

        try {
            cost = Double.parseDouble(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid service cost.");
            return;
        }

        if (!Validation.isValidCost(cost)) {
            System.out.println("Service cost cannot be negative.");
            return;
        }

        String sql = "INSERT INTO service_records(vehicle_id, service_date, description, cost) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, vehicleId);
            ps.setDate(2, serviceDate);
            ps.setString(3, description);
            ps.setDouble(4, cost);
            ps.executeUpdate();
            System.out.println("Service booked successfully.");
        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            System.out.println("Vehicle ID does not exist.");
        }
    }

    // 6. View service history using JOIN
    public static void viewServiceHistory(Connection con) throws Exception {

        String sql = "SELECT sr.service_id, c.name, v.vehicle_number, v.model, sr.service_date, sr.description, sr.cost, sr.status FROM service_records sr JOIN vehicles v ON sr.vehicle_id = v.vehicle_id JOIN customers c ON v.customer_id = c.customer_id ORDER BY sr.service_date DESC, sr.service_id DESC";

        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\nVEHICLE SERVICE HISTORY");
            System.out.printf("%-10s %-18s %-16s %-18s %-12s %-24s %-12s %-14s%n", "Service ID", "Customer", "Vehicle Number", "Model", "Date", "Description", "Cost", "Status");
            System.out.println("--------------------------------------------------------------------------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-10d %-18s %-16s %-18s %-12s %-24s %-12.2f %-14s%n", rs.getInt("service_id"), rs.getString("name"), rs.getString("vehicle_number"), rs.getString("model"), rs.getDate("service_date"), rs.getString("description"), rs.getDouble("cost"), rs.getString("status"));
            }
        }
    }

    // 7. Search vehicle
    public static void searchVehicle(Connection con, Scanner sc) throws Exception {

        System.out.print("Enter vehicle number to search: ");
        String vehicleNumber = sc.nextLine().trim().toUpperCase();

        String sql = "SELECT c.customer_id, c.name, c.phone, c.email, v.vehicle_id, v.vehicle_number, v.model FROM vehicles v JOIN customers c ON v.customer_id = c.customer_id WHERE v.vehicle_number = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, vehicleNumber);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("\nVEHICLE SEARCH RESULT");
                    System.out.println("Customer ID    : " + rs.getInt("customer_id"));
                    System.out.println("Customer Name  : " + rs.getString("name"));
                    System.out.println("Phone          : " + rs.getString("phone"));
                    System.out.println("Email          : " + rs.getString("email"));
                    System.out.println("Vehicle ID     : " + rs.getInt("vehicle_id"));
                    System.out.println("Vehicle Number : " + rs.getString("vehicle_number"));
                    System.out.println("Model          : " + rs.getString("model"));
                } else {
                    System.out.println("Vehicle not found.");
                }
            }
        }
    }

    // 8. Search service history
    public static void searchServiceHistory(Connection con, Scanner sc) throws Exception {

        System.out.print("Enter vehicle number: ");
        String vehicleNumber = sc.nextLine().trim().toUpperCase();

        String sql = "SELECT c.name, v.vehicle_number, v.model, sr.service_id, sr.service_date, sr.description, sr.cost, sr.status FROM service_records sr JOIN vehicles v ON sr.vehicle_id = v.vehicle_id JOIN customers c ON v.customer_id = c.customer_id WHERE v.vehicle_number = ? ORDER BY sr.service_date DESC";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, vehicleNumber);

            try (ResultSet rs = ps.executeQuery()) {
                System.out.printf("%-18s %-16s %-18s %-10s %-12s %-22s %-12s %-14s%n", "Customer", "Vehicle Number", "Model", "Service ID", "Date", "Description", "Cost", "Status");
                System.out.println("--------------------------------------------------------------------------------------------------------------------------------");

                boolean found = false;

                while (rs.next()) {
                    found = true;
                    System.out.printf("%-18s %-16s %-18s %-10d %-12s %-22s %-12.2f %-14s%n", rs.getString("name"), rs.getString("vehicle_number"), rs.getString("model"), rs.getInt("service_id"), rs.getDate("service_date"), rs.getString("description"), rs.getDouble("cost"), rs.getString("status"));
                }

                if (!found) {
                    System.out.println("No service records found for this vehicle.");
                }
            }
        }
    }
}
