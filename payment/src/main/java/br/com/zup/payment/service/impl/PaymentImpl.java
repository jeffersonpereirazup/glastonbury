package br.com.zup.payment.service.impl;

import br.com.zup.payment.event.ReserveCreatedEvent;
import br.com.zup.payment.service.Payment;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PaymentImpl implements Payment {

    private ObjectMapper objectMapper;
    private KafkaTemplate<String, ReserveCreatedEvent> template;

    @Autowired
    public PaymentImpl(ObjectMapper objectMapper, KafkaTemplate<String, ReserveCreatedEvent> template) {
        this.objectMapper = objectMapper;
        this.template = template;
    }

    @KafkaListener(topics = "created-payments", groupId = "payment-group-id")
    public void listen(String message) throws IOException {

        ReserveCreatedEvent event = this.objectMapper.readValue(message, ReserveCreatedEvent.class);
        System.out.println(">>> Received payment event from inventory topic: " + event.getCustomerId());

        try {
            Thread.sleep(1000);
            confirmOrder(event);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void confirmOrder(ReserveCreatedEvent event) {
        this.template.send("confirm-order", event);
    }
}
