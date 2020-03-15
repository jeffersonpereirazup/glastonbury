package br.com.zup.inventory.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "tickets")
public class Ticket {

    @Id
    private String id;
    private String day;
    private Integer quantity;

    public Ticket() {
    }

    public Ticket(String id, String day, Integer quantity) {
        this.id = id;
        this.day = day;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
