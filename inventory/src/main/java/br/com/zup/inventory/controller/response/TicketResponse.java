package br.com.zup.inventory.controller.response;

import br.com.zup.inventory.entity.Ticket;

public class TicketResponse {

    private String id;
    private String day;
    private Integer quantity;

    public TicketResponse() {
    }

    public TicketResponse(String id, String day, Integer quantity) {
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

    public static TicketResponse fromEntity(Ticket ticket){
        return new TicketResponse(
                ticket.getId(),
                ticket.getDay(),
                ticket.getQuantity()
        );
    }
}
