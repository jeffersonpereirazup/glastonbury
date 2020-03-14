package br.com.zup.inventory.event;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class OrderCreatedEvent {

    private String orderId;
    private String customerId;
    private BigDecimal amount;
    private List<String> itemIds;
    private Map<String, Integer> quantityPerDay;

    public OrderCreatedEvent() {
    }

    public OrderCreatedEvent(String orderId, String customerId, BigDecimal amount, List<String> itemIds, Map<String, Integer> quantityPerDay) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.amount = amount;
        this.itemIds = itemIds;
        this.quantityPerDay = quantityPerDay;
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

    public Map<String, Integer> getQuantityPerDay() {
        return quantityPerDay;
    }

    public void setQuantityPerDay(Map<String, Integer> quantityPerDay) {
        this.quantityPerDay = quantityPerDay;
    }
}
