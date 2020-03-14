package br.com.zup.inventory.service;

import br.com.zup.inventory.event.OrderCreatedEvent;

public interface TransactionService {

    void notEnoughTickets();
    void generatePayment();
    void ticketsAreOver();
    void buyTicket(OrderCreatedEvent event);
}
