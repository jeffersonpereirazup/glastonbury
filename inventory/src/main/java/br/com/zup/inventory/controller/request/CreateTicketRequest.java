package br.com.zup.inventory.controller.request;

import br.com.zup.inventory.entity.Ticket;

import java.util.UUID;

public class CreateTicketRequest {
    private String id;
    private String day;
    private Integer quantity;

    public CreateTicketRequest() {
    }

    public CreateTicketRequest(String id, String day, Integer quantity) {
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

    public Ticket toEntity(){
        return new Ticket(
                UUID.randomUUID().toString(),
                this.day,
                this.quantity
        );
    }
}
