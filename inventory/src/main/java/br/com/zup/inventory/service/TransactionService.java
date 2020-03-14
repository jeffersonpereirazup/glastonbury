package br.com.zup.inventory.service;

import br.com.zup.inventory.event.OrderCreatedEvent;
import br.com.zup.inventory.event.ReserveCreatedEvent;

public interface TransactionService {

    void generatePayment(ReserveCreatedEvent event);
    void thereIsNoEnoughTickets(ReserveCreatedEvent event);
    void buyTicket(OrderCreatedEvent event);
}
