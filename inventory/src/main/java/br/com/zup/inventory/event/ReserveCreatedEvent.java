package br.com.zup.inventory.event;

public class ReserveCreatedEvent {

    private String ticketId;
    private Integer quantity;
    private String day;
    private String customerId;
    private String log;

    public ReserveCreatedEvent() {
    }

    public ReserveCreatedEvent(String ticketId, Integer quantity, String day, String customerId, String log) {
        this.ticketId = ticketId;
        this.quantity = quantity;
        this.day = day;
        this.customerId = customerId;
        this.log = log;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}
