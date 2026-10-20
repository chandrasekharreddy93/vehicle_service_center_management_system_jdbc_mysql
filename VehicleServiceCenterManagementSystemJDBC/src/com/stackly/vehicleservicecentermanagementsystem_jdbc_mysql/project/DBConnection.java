package com.stackly.vehicleservicecentermanagementsystem_jdbc_mysql.project;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    static final String url = "jdbc:mysql://localhost:3306/vehicle_service_db";
    static final String user = "root";
    static final String pass = "Chandra@9392518796@@";

    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, user, pass);
        return con;
    }
}
