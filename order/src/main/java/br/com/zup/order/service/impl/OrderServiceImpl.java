package br.com.zup.order.service.impl;

import br.com.zup.order.controller.request.CreateOrderRequest;
import br.com.zup.order.controller.response.OrderResponse;
import br.com.zup.order.event.OrderCreatedEvent;
import br.com.zup.order.repository.OrderRepository;
import br.com.zup.order.service.OrderService;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private KafkaTemplate<String, OrderCreatedEvent> template;
    private Tracer tracer;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, KafkaTemplate<String, OrderCreatedEvent> template, Tracer tracer) {
        this.orderRepository = orderRepository;
        this.template = template;
        this.tracer = tracer;
    }

    @Override
    public String save(CreateOrderRequest request) {

        // Create a span
        Span span = this.tracer.buildSpan("create-order-event-send").start();

        span.setTag("order.customer_id", request.getCustomerId());

        String orderId = this.orderRepository.save(request.toEntity()).getId();

        OrderCreatedEvent event = new OrderCreatedEvent(
                orderId,
                request.getCustomerId(),
                request.getAmount(),
                createItemMap(request)
        );

        this.template.send("created-orders", event);

        span.finish();

        return orderId;
    }

    private Map<String, Integer> createItemMap(CreateOrderRequest request) {
        Map<String, Integer> result = new HashMap<>();
        for (CreateOrderRequest.OrderItemPart item : request.getItems()) {
            result.put(item.getId(), item.getQuantity());
        }

        return result;
    }

    @Override
    public List<OrderResponse> findAll() {
        return this.orderRepository.findAll()
                .stream()
                .map(OrderResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
