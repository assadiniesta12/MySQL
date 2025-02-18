import java.sql.*;
import java.util.Scanner;

public class InsertUserData {
    
    // Function to connect to the database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/user_management",
                "root",
                "London12@" // Change to your MySQL password
        );
    }
    
    // Function to check if email already exists
    public static boolean emailExists(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; // Returns true if email exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error checking for duplicate email: " + e.getMessage());
        }
        return false;
    }
    
    // Function to insert user data (with email duplicate check)
    public static void insertUserData(String name, String surename, String email) {
        // Check for duplicate email
        if (emailExists(email)) {
            System.out.println("Error: A user with the same email already exists!");
            return; // Stop the insert process
        }
        
        // Insert data if email is unique
        String query = "INSERT INTO users (name, surename, email) VALUES (?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surename);
            preparedStatement.setString(3, email);
            
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("User data inserted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error inserting data: " + e.getMessage());
        }
    }
    
    // Main method to get user input
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter surename: ");
        String surename = scanner.nextLine();
        
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        
        // Call reusable method
        insertUserData(name, surename, email);
        
        scanner.close();
    }
}
