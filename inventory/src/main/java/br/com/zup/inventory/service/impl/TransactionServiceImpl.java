package br.com.zup.inventory.service.impl;

import br.com.zup.inventory.entity.Ticket;
import br.com.zup.inventory.event.OrderCreatedEvent;
import br.com.zup.inventory.event.ReserveCreatedEvent;
import br.com.zup.inventory.repository.TicketRepository;
import br.com.zup.inventory.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private TicketRepository ticketRepository;
    private KafkaTemplate<String, ReserveCreatedEvent> template;
    private ObjectMapper objectMapper;

    @Autowired
    public TransactionServiceImpl(TicketRepository ticketRepository, KafkaTemplate<String, ReserveCreatedEvent> template, ObjectMapper objectMapper) {
        this.ticketRepository = ticketRepository;
        this.template = template;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "created-orders", groupId = "inventory-group-id")
    public void listen(String message) throws IOException {

        OrderCreatedEvent event = this.objectMapper.readValue(message, OrderCreatedEvent.class);
        this.buyTicket(event);
        System.out.println(">>> Received inventory event from create-orders topic: " + event.getCustomerId());
    }

    @Override
    public void generatePayment(ReserveCreatedEvent event) {
        this.template.send("created-payments", event);
    }

    @Override
    public void thereIsNoEnoughTickets(ReserveCreatedEvent event) {
        this.template.send("cancel-order", event);
    }

    @Override
    public void buyTicket(OrderCreatedEvent event) {

        Ticket tickets = this.ticketRepository.findAll().get(0);
        Map.Entry<String, Integer> ticketItem = event.getItems().entrySet().iterator().next();
        Integer totalOfticketsForBuy = ticketItem.getValue();

        if(tickets.getQuantity().compareTo(totalOfticketsForBuy) >= 0)
        {
            Optional<Ticket> item = this.ticketRepository.findById(tickets.getId());
            item.get().setQuantity(tickets.getQuantity() - totalOfticketsForBuy);
            this.ticketRepository.save(item.get());

            ReserveCreatedEvent reserveCreatedEvent = new ReserveCreatedEvent(
                    event.getOrderId(),
                    totalOfticketsForBuy,
                    tickets.getDay(),
                    event.getCustomerId(),
                    "ticket-was-booked"
            );

            generatePayment(reserveCreatedEvent);
        }
        else
        {
            ReserveCreatedEvent reserveCreatedEvent = new ReserveCreatedEvent(
                    event.getOrderId(),
                    totalOfticketsForBuy,
                    tickets.getDay(),
                    event.getCustomerId(),
                    "there-is-no-enough-tickets"
            );

            thereIsNoEnoughTickets(reserveCreatedEvent);
        }
    }
}
