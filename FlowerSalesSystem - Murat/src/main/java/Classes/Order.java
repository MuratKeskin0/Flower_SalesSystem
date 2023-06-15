/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

/**
 *
 * @author murat
 */
import java.util.List;

public class Order {

    private int orderId;
    private int userId;
    private String deliveryAddress;

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
   private List<OrderItem> orderItems;

    public Order(int orderId, int userId, String deliveryAddress) {
        this.orderId = orderId;
        this.userId = userId;
        this.deliveryAddress = deliveryAddress;

    }

    public Order(String userName, String email, String deliveryAddress) {

    }

    public Order(int userId, String deliveryAddress) {
        this.userId = userId;
        this.deliveryAddress = deliveryAddress;
    }

    // Getter ve setter metodları
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int id) {
        this.orderId = id;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
