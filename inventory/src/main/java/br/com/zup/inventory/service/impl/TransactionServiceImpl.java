package br.com.zup.inventory.service.impl;

import br.com.zup.inventory.entity.Ticket;
import br.com.zup.inventory.event.OrderCreatedEvent;
import br.com.zup.inventory.event.ReserveCreatedEvent;
import br.com.zup.inventory.repository.TicketRepository;
import br.com.zup.inventory.service.TransactionService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private TicketRepository ticketRepository;
    private KafkaTemplate<String, ReserveCreatedEvent> template;

    public TransactionServiceImpl(TicketRepository ticketRepository, KafkaTemplate<String, ReserveCreatedEvent> template) {
        this.ticketRepository = ticketRepository;
        this.template = template;
    }

    @Override
    public void notEnoughTickets() {

    }

    @Override
    public void generatePayment() {

    }

    @Override
    public void ticketsAreOver() {

    }

    @Override
    public void buyTicket(OrderCreatedEvent event) {

        event.getQuantityPerDay();

    }
}
