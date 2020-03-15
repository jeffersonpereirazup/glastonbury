package br.com.zup.inventory.service;

import br.com.zup.inventory.event.OrderCreatedEvent;

public interface InventoryService {

    String listener(OrderCreatedEvent event);
}
