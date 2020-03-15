package br.com.zup.payment.service.impl;

import br.com.zup.payment.event.ReserveCreatedEvent;
import br.com.zup.payment.service.Payment;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PaymentImpl implements Payment {

    private ObjectMapper objectMapper;
    private KafkaTemplate<String, ReserveCreatedEvent> template;
    private Tracer tracer;

    @Autowired
    public PaymentImpl(ObjectMapper objectMapper, KafkaTemplate<String, ReserveCreatedEvent> template, Tracer tracer) {
        this.objectMapper = objectMapper;
        this.template = template;
        this.tracer = tracer;
    }

    @KafkaListener(topics = "created-payments", groupId = "payment-group-id")
    public void listen(String message) throws IOException {

        Span span = this.tracer.buildSpan("created-payments-event-listener").start();

        ReserveCreatedEvent event = this.objectMapper.readValue(message, ReserveCreatedEvent.class);

        span.setTag("order.customer_id", event.getCustomerId());

        System.out.println(">>> Received payment event from inventory topic: " + event.getCustomerId());

        try {
            Thread.sleep(500);
            confirmOrder(event);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        span.finish();
    }

    @Override
    public void confirmOrder(ReserveCreatedEvent event) {

        Span span = this.tracer.buildSpan("confirm-order-event-listener").start();

        span.setTag("order.customer_id", event.getCustomerId());

        this.template.send("confirm-order", event);

        span.finish();

    }
}
