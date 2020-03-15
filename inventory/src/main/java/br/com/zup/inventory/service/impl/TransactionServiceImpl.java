package br.com.zup.inventory.service.impl;

import br.com.zup.inventory.entity.Ticket;
import br.com.zup.inventory.event.OrderCreatedEvent;
import br.com.zup.inventory.event.ReserveCreatedEvent;
import br.com.zup.inventory.repository.TicketRepository;
import br.com.zup.inventory.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.opentracing.Span;
import io.opentracing.Tracer;
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
    private Tracer tracer;

    @Autowired
    public TransactionServiceImpl(TicketRepository ticketRepository, KafkaTemplate<String, ReserveCreatedEvent> template, ObjectMapper objectMapper, Tracer tracer) {
        this.ticketRepository = ticketRepository;
        this.template = template;
        this.objectMapper = objectMapper;
        this.tracer = tracer;
    }

    @KafkaListener(topics = "created-orders", groupId = "inventory-group-id")
    public void listen(String message) throws IOException {

        // Create a span
        Span span = this.tracer.buildSpan("created-orders-event").start();

        OrderCreatedEvent event = this.objectMapper.readValue(message, OrderCreatedEvent.class);

        span.setTag("order.customer_id", event.getCustomerId());

        Span spanCreateTicket = this.tracer.buildSpan("buy-ticket").asChildOf(span).start();

        this.buyTicket(event);

        System.out.println(">>> Received inventory event from create-orders topic: " + event.getCustomerId());

        spanCreateTicket.finish();

        span.finish();
    }

    @Override
    public void generatePayment(ReserveCreatedEvent event) {

        Span span = this.tracer.buildSpan("created-payments-event-send").start();

        span.setTag("order.customer_id", event.getCustomerId());

        this.template.send("created-payments", event);

        span.finish();
    }

    @Override
    public void thereIsNoEnoughTickets(ReserveCreatedEvent event) {

        Span span = this.tracer.buildSpan("cancel-order-event-send").start();

        this.template.send("cancel-order", event);

        span.finish();
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
