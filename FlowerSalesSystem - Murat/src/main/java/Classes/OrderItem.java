/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

/**
 *
 * @author murat
 */
public class OrderItem {
    private int id;
    private int orderId;
    private String flowerName;
    private int quantity;
    private double price;

    public OrderItem(int id, int orderId, String flowerName, int quantity, double price) {
        this.id = id;
        this.orderId = orderId;
        this.flowerName = flowerName;
        this.quantity = quantity;
        this.price = price;
    }

   

    public int getId() {
        return id;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getFlowerName() {
        return flowerName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
    
}
