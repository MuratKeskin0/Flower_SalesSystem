/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Databases;

/**
 *
 * @author murat
 */
import Classes.User;
import java.sql.*;
import java.util.ArrayList;

public class UserDatabaseManager {

    public static String connectionString = "jdbc:mysql://localhost:3306/flower_system_db?user=root&password=root";
    public static Connection conn;

    public static User login(User user) {
        try {
            conn = DriverManager.getConnection(connectionString);
            java.sql.Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM flower_system_db.users WHERE email = '" + user.email + "' AND password = '" + user.password + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                User authenticatedUser = new User(rs.getString("username"), rs.getString("password"), rs.getString("email"));
                authenticatedUser.setId(rs.getInt("id")); // add this line to set the id
                conn.close();
                return authenticatedUser;
            }
            conn.close();
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static boolean register(User user) {
        try {
            conn = DriverManager.getConnection(connectionString);
            java.sql.Statement stmt = conn.createStatement();
            String sql = "INSERT INTO flower_system_db.users (username, password, email) VALUES ('" + user.username + "', '" + user.password + "', '" + user.email + "')";
            stmt.executeUpdate(sql);
            conn.close();
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    public static boolean addToBasket(int userId, int flowerId, int quantity) {
        boolean isSuccessful = false;
        try ( Connection connection = DriverManager.getConnection(connectionString)) {
            // check if the user exists
            String userCheckQuery = "SELECT * FROM users WHERE id = ?";
            PreparedStatement userCheckStatement = connection.prepareStatement(userCheckQuery);
            userCheckStatement.setInt(1, userId);
            ResultSet userRs = userCheckStatement.executeQuery();
            if (userRs.next()) { // if the user exists
                // check if the basket already has this flower for this user
                String checkQuery = "SELECT * FROM baskets WHERE id = ? AND idflowers = ?";
                PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
                checkStatement.setInt(1, userId);
                checkStatement.setInt(2, flowerId);
                ResultSet rs = checkStatement.executeQuery();
                if (rs.next()) {
                    // if the flower is already in the basket, update the quantity
                    int oldQuantity = rs.getInt("quantity");
                    String updateQuery = "UPDATE baskets SET quantity = ? WHERE id = ? AND idflowers = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                    updateStatement.setInt(1, oldQuantity + quantity);
                    updateStatement.setInt(2, userId);
                    updateStatement.setInt(3, flowerId);
                    int affectedRows = updateStatement.executeUpdate();
                    if (affectedRows > 0) {
                        isSuccessful = true;
                    }
                } else {
                    // if the flower is not in the basket, add it
                    String insertQuery = "INSERT INTO baskets (id, idflowers, quantity) VALUES (?, ?, ?)";
                    PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                    insertStatement.setInt(1, userId);
                    insertStatement.setInt(2, flowerId);
                    insertStatement.setInt(3, quantity);
                    int affectedRows = insertStatement.executeUpdate();
                    if (affectedRows > 0) {
                        isSuccessful = true;
                    }
                }
            } else {
                System.out.println("The user with id " + userId + " does not exist.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isSuccessful;
    }

    public static boolean removeFromBasket(int userId, int flowerId, int quantity) {
        boolean isSuccessful = false;
        try ( Connection connection = DriverManager.getConnection(connectionString)) {
            // check if the user exists
            String userCheckQuery = "SELECT * FROM users WHERE id = ?";
            PreparedStatement userCheckStatement = connection.prepareStatement(userCheckQuery);
            userCheckStatement.setInt(1, userId);
            ResultSet userRs = userCheckStatement.executeQuery();
            if (userRs.next()) { // if the user exists
                // check if the basket has this flower for this user
                String checkQuery = "SELECT * FROM baskets WHERE id = ? AND idflowers = ?";
                PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
                checkStatement.setInt(1, userId);
                checkStatement.setInt(2, flowerId);
                ResultSet rs = checkStatement.executeQuery();
                if (rs.next()) {
                    // if the flower is in the basket, decrease the quantity
                    int oldQuantity = rs.getInt("quantity");
                    // If the quantity to remove is equal to or greater than the existing quantity,
                    // delete the item from the basket.
                    if (quantity >= oldQuantity) {
                        String deleteQuery = "DELETE FROM baskets WHERE id = ? AND idflowers = ?";
                        PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                        deleteStatement.setInt(1, userId);
                        deleteStatement.setInt(2, flowerId);
                        int affectedRows = deleteStatement.executeUpdate();
                        if (affectedRows > 0) {
                            isSuccessful = true;
                        }
                    } else {
                        // Otherwise, update the quantity
                        String updateQuery = "UPDATE baskets SET quantity = ? WHERE id = ? AND idflowers = ?";
                        PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                        updateStatement.setInt(1, oldQuantity - quantity);
                        updateStatement.setInt(2, userId);
                        updateStatement.setInt(3, flowerId);
                        int affectedRows = updateStatement.executeUpdate();
                        if (affectedRows > 0) {
                            isSuccessful = true;
                        }
                    }
                } else {
                    System.out.println("The flower with id " + flowerId + " is not in the basket of user with id " + userId);
                }
            } else {
                System.out.println("The user with id " + userId + " does not exist.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isSuccessful;
    }

    public static int getUserIdByEmail(String email) {
        int userId = 0;

        try ( Connection connection = DriverManager.getConnection(connectionString)) {
            String query = "SELECT id FROM users WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                userId = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userId;
    }

    public static ArrayList<User> getUsers() {
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try ( Connection connection = DriverManager.getConnection(connectionString);  PreparedStatement statement = connection.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String username = resultSet.getString("username");

                String email = resultSet.getString("email");
                String password = resultSet.getString("password");

                User user = new User(username, email, password);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public static boolean deleteUser(String email) {
        boolean isDeleted = false;
        Connection conn = null;
        PreparedStatement pst = null;

        try {
            conn = DriverManager.getConnection(connectionString);
            String query = "DELETE FROM users WHERE email = ?";
            pst = conn.prepareStatement(query);
            pst.setString(1, email);

            int result = pst.executeUpdate();
            if (result > 0) {
                isDeleted = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return isDeleted;
    }

    public static User getUserByEmail(String email) {
        User user = null;
        String sql = "SELECT * FROM users WHERE email = ?";

        try ( Connection connection = DriverManager.getConnection(connectionString);  PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");

                user = new User(username, password, email);
                user.setId(resultSet.getInt("id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

}
