package com.stackly.vehicleservicecentermanagementsystem_jdbc_mysql.project;

import java.sql.Connection;
import java.sql.Statement;

public class TableCreation {

    public static void createTables(Connection con) throws Exception {

        Statement st = con.createStatement();

        String customers = "CREATE TABLE IF NOT EXISTS customers (customer_id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100) NOT NULL, email VARCHAR(100) UNIQUE NOT NULL, phone VARCHAR(15) NOT NULL)";

        String vehicles = "CREATE TABLE IF NOT EXISTS vehicles (vehicle_id INT PRIMARY KEY AUTO_INCREMENT, customer_id INT NOT NULL, vehicle_number VARCHAR(20) UNIQUE NOT NULL, model VARCHAR(100) NOT NULL, FOREIGN KEY (customer_id) REFERENCES customers(customer_id))";

        String serviceRecords = "CREATE TABLE IF NOT EXISTS service_records (service_id INT PRIMARY KEY AUTO_INCREMENT, vehicle_id INT NOT NULL, service_date DATE NOT NULL, description VARCHAR(255) NOT NULL, cost DECIMAL(10,2) NOT NULL, status ENUM('Booked','In Progress','Completed','Cancelled') DEFAULT 'Booked', FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id))";

        st.executeUpdate(customers);
        st.executeUpdate(vehicles);
        st.executeUpdate(serviceRecords);

        st.close();

        System.out.println("All tables created successfully.");
    }
}
