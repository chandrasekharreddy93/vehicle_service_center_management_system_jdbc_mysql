package com.stackly.vehicleservicecentermanagementsystem_jdbc_mysql.project;

public class Validation {

    public static boolean isValidName(String name) {
        return name != null && name.trim().matches("[a-zA-Z ]+");
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("[0-9]{10,15}");
    }

    public static boolean isValidVehicleNumber(String number) {
        return number != null && number.trim().matches("[A-Za-z0-9 -]{4,20}");
    }

    public static boolean isValidModel(String model) {
        return model != null && !model.trim().isEmpty();
    }

    public static boolean isValidCost(double cost) {
        return Double.isFinite(cost) && cost >= 0;
    }

    public static boolean isValidDescription(String description) {
        return description != null && !description.trim().isEmpty();
    }
}
