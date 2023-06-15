/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

/**
 *
 * @author murat
 */
import java.util.ArrayList;
import java.util.List;

public class Basket {
    public int id;
    public List<Flower> flowers;

    public Basket() {
        this.flowers = new ArrayList<>();
    }

    public void addFlower(Flower flower) {
        flowers.add(flower);
    }

    public void removeFlower(Flower flower) {
        flowers.remove(flower);
    }

    public void clear() {
        flowers.clear();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Flower> getFlowers() {
        return flowers;
    }

    public double getTotal() {
        double total = 0;
        for(Flower flower: flowers){
            total += flower.getPrice();
        }
        return total;
    }

    
      
}



