/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Databases;

/**
 *
 * @author murat
 */
import Classes.Flower;
import Classes.Basket;
import Classes.BasketItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BasketDatabaseManager {

    public static String connectionString = "jdbc:mysql://localhost:3306/flower_system_db?user=root&password=root";
    public static Connection conn;

    private FlowerDatabaseManager flowerDBManager = new FlowerDatabaseManager(conn);

    public void updateBasket(Basket basket) {
        String sql = "UPDATE baskets SET total = ? WHERE id = ?";

        try ( PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setDouble(1, basket.getTotal());
            statement.setInt(2, basket.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBasket(String id) {
        String sql = "DELETE FROM baskets WHERE id = ?";

        try ( PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeFlowerFromBasket(String basketId, Flower flower) {
        String sql = "DELETE FROM baskets_flowers WHERE basketId = ? AND flowerId = ?";

        try ( PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, basketId);
            statement.setString(2, flower.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean clearBasket(int userId) {
        boolean isSuccessful = false;
        String sql = "DELETE FROM baskets WHERE id = ?";

        try ( Connection connection = DriverManager.getConnection(connectionString);  PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                isSuccessful = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isSuccessful;
    }

    public static List<BasketItem> getUserBasket(int userId) {
        List<BasketItem> basketItems = new ArrayList<>();
        String sql = "SELECT bf.basketId, f.idflowers as flower_id, f.nameflowers as flower_name, f.priceflowers as flower_price, bf.quantity "
                + "FROM baskets bf "
                + "INNER JOIN flowers f ON bf.idflowers = f.idflowers "
                + "WHERE bf.id = ?";

        try ( Connection connection = DriverManager.getConnection(connectionString);  PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String flowerId = resultSet.getString("flower_id");
                String flowerName = resultSet.getString("flower_name");
                double flowerPrice = resultSet.getDouble("flower_price");
                int quantity = resultSet.getInt("quantity");

                Flower flower = new Flower(flowerId, flowerName, flowerPrice, quantity);
                BasketItem basketItem = new BasketItem(flower, quantity);
                basketItems.add(basketItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return basketItems;
    }

    public static void updateBasketItemQuantity(int userId, int flowerId, int quantity) {
        String sql = "UPDATE baskets SET id = ? WHERE idflowers = ? AND quantity = ?";

        try ( Connection connection = DriverManager.getConnection(connectionString);  PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, flowerId);
            statement.setInt(3, quantity);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void deleteBasketItem(int userId, int flowerId) {
        String sql = "DELETE FROM baskets WHERE id = ? AND idflowers = ?";

        try ( Connection connection = DriverManager.getConnection(connectionString);  PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, flowerId);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
