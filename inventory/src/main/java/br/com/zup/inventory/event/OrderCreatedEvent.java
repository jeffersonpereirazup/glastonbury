package br.com.zup.inventory.event;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class OrderCreatedEvent {

    private String orderId;
    private String customerId;
    private BigDecimal amount;
    private List<String> itemIds;
    private Integer quantity;

    public OrderCreatedEvent() {
    }

    public OrderCreatedEvent(String orderId, String customerId, BigDecimal amount, List<String> itemIds, Integer quantity) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.amount = amount;
        this.itemIds = itemIds;
        this.quantity = quantity;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public List<String> getItemIds() {
        return itemIds;
    }

    public void setItemIds(List<String> itemIds) {
        this.itemIds = itemIds;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
