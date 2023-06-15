/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Databases;

/**
 *
 * @author murat
 */
import Classes.Order;
import Classes.OrderItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDatabaseManager {

    public static String connectionString = "jdbc:mysql://localhost:3306/flower_system_db?user=root&password=root";
    public static Connection conn;

    public static Order getOrderById(int orderId) {
        Order order = null;
        try {
            conn = DriverManager.getConnection(connectionString);
            String sql = "SELECT * FROM orders WHERE idorders = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, orderId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                order = new Order(rs.getInt("idorders"), rs.getInt("id"), rs.getString("deliveryadress"));
            }
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    public static List<OrderItem> getOrderItems(int userId) {
        List<OrderItem> orderItems = new ArrayList<>();
        try {
            conn = DriverManager.getConnection(connectionString);
            String sql = "SELECT * FROM baskets WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String flowerName = getFlowerName(rs.getInt("idflowers"));
                double price = getFlowerPrice(rs.getInt("idflowers"));
                OrderItem item = new OrderItem(rs.getInt("id"), rs.getInt("idorders"), flowerName, rs.getInt("quantity"), price);
                orderItems.add(item);
            }
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderItems;
    }

    public static String getFlowerName(int flowerId) {
        String flowerName = "";
        try {
            conn = DriverManager.getConnection(connectionString);
            String sql = "SELECT * FROM flowers WHERE idflowers = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, flowerId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                flowerName = rs.getString("nameflowers");
            }
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flowerName;
    }

    public static double getFlowerPrice(int flowerId) {
        double price = 0.0;
        try {
            conn = DriverManager.getConnection(connectionString);
            String sql = "SELECT * FROM flowers WHERE idflowers = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, flowerId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                price = rs.getDouble("priceflowers");
            }
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return price;
    }

    public static boolean saveOrderItem(OrderItem orderItem) {
        Connection conn = null;
        PreparedStatement pst = null;
        boolean isSuccessful = false;
        try {
            // Establish a connection to the database
            conn = DriverManager.getConnection(connectionString);

            // Prepare the SQL query
            String query = "INSERT INTO orderdetails (id,idorders, flower_name, quantity, price) VALUES (?,?, ?, ?, ?)";
            pst = conn.prepareStatement(query);

            // Set the values to be inserted
            pst.setInt(1, orderItem.getId());
            pst.setInt(2, orderItem.getOrderId());
            pst.setString(3, orderItem.getFlowerName());
            pst.setInt(4, orderItem.getQuantity());
            pst.setDouble(5, orderItem.getPrice());

            // Execute the query
            int result = pst.executeUpdate();

            // Check if the insertion was successful
            isSuccessful = result > 0;

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
        return isSuccessful;
    }

    public static List<OrderItem> getOrderItemsByOrderId(int orderId) {
        List<OrderItem> orderItems = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(connectionString);

            // Your SQL query to get order items by order ID
            String sql = "SELECT * FROM orderdetails WHERE idorders = ?"; // Assuming 'orderdetails' is the name of the table storing order items
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, orderId);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                int itemId = rs.getInt("id");
                String flowerName = rs.getString("flower_name");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");

                // Create OrderItem object and add it to the list
                OrderItem item = new OrderItem(itemId, orderId, flowerName, quantity, price);
                orderItems.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Closing resources
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return orderItems;
    }

    public static boolean saveOrderAddress(String Email, String deliveryAddress) {
        boolean isSuccessful = false;
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            // Veritabanı bağlantısı oluşturuluyor
            conn = DriverManager.getConnection(connectionString);
            // Sorgu oluşturuluyor
            String query = "INSERT INTO orders (id, deliveryadress) VALUES (?, ?)";
            pst = conn.prepareStatement(query);

            // Kullanıcı adı ile ilgili kullanıcının id'sini al
            int userId = UserDatabaseManager.getUserIdByEmail(Email);
            if (userId == 0) {
                System.out.println("The user with username " + Email + " does not exist.");
                return false;
            }
            pst.setInt(1, userId);
            pst.setString(2, deliveryAddress);

            // Sorgu çalıştırılıyor
            int result = pst.executeUpdate();

            if (result > 0) {
                System.out.println("Address saved successfully");
                isSuccessful = true;
            } else {
                System.out.println("An error occurred");
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
        return isSuccessful;
    }

    public static ArrayList<Order> getOrders() {
        ArrayList<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders";

        try ( Connection connection = DriverManager.getConnection(connectionString);  PreparedStatement statement = connection.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int orderId = resultSet.getInt("idorders");
                int userId = resultSet.getInt("id");
                String deliveryAddress = resultSet.getString("deliveryadress");

                Order order = new Order(orderId, userId, deliveryAddress);
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    public static boolean saveOrder(Order order) {
        try ( Connection connection = DriverManager.getConnection(connectionString);  PreparedStatement statement = connection.prepareStatement("INSERT INTO orders (id, deliveryadress) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, order.getUserId());
            statement.setString(2, order.getDeliveryAddress());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                try ( ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        order.setOrderId(generatedKeys.getInt(1));
                    }
                }
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteOrderById(int orderId) {
        String sql = "DELETE FROM orders WHERE idorders = ?";
        boolean isSuccessful = false;

        try ( Connection connection = DriverManager.getConnection(connectionString);  PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, orderId);
            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                isSuccessful = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isSuccessful;
    }

    public static ArrayList<Order> getOrdersByEmail(String email) {
        ArrayList<Order> orders = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(connectionString);

            // Get user ID by email
            int userId = UserDatabaseManager.getUserIdByEmail(email);
            if (userId == 0) {
                System.out.println("The user with email " + email + " does not exist.");
                return orders;
            }

            // Prepare the SQL query to get orders by user ID
            String query = "SELECT * FROM orders WHERE id = ?";
            pst = conn.prepareStatement(query);

            // Set the user ID
            pst.setInt(1, userId);

            // Execute the query
            rs = pst.executeQuery();

            // Process the result set
            while (rs.next()) {
                int orderId = rs.getInt("idorders");
                String deliveryAddress = rs.getString("deliveryadress");

                Order order = new Order(orderId, userId, deliveryAddress);
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Closing resources
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return orders;
    }

}
