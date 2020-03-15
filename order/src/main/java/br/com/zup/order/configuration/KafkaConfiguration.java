package br.com.zup.order.configuration;

import br.com.zup.order.event.OrderCreatedEvent;
import br.com.zup.order.event.ReserveCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrap;
    private ObjectMapper objectMapper;
    private Tracer tracer;

    public KafkaConfiguration(@Value(value = "${spring.kafka.bootstrap-servers}") String bootstrap,
                              ObjectMapper objectMapper, Tracer tracer) {
        this.bootstrap = bootstrap;
        this.objectMapper = objectMapper;
        this.tracer = tracer;
    }


    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic message() {
        return new NewTopic("created-orders", 1, (short) 1);
    }

    @Bean
    public NewTopic topicToConfirmOrder() {
        return new NewTopic("confirm-order", 1, (short) 1);
    }

    @Bean
    public DefaultKafkaProducerFactory messageProducerFactory() {

        Map<String, Object> configProps = new HashMap<>();

        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, OrderCreatedEvent> messageKafkaTemplate() {
        return new KafkaTemplate<String, OrderCreatedEvent>(messageProducerFactory());
    }

    @Bean
    public KafkaTemplate<String, ReserveCreatedEvent> templateToConfirmOrder() {
        return new KafkaTemplate<String, ReserveCreatedEvent>(messageProducerFactory());
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "order-group-id");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String>
    kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @KafkaListener(topics = "cancel-order", groupId = "order-group-id")
    public void listenConfirmation(String message) throws IOException {

        Span span = this.tracer.buildSpan("cancel-order-event-listener").start();

        OrderCreatedEvent event = this.objectMapper.readValue(message, OrderCreatedEvent.class);

        span.setTag("order.customer_id", event.getCustomerId());

        System.out.println(">>> Received cancel order event from inventory topic: " + event.getCustomerId());

        span.finish();
    }

    @KafkaListener(topics = "confirm-order", groupId = "order-group-id")
    public void listen(String message) throws IOException {

        Span span = this.tracer.buildSpan("confirm-order-event-listener").start();

        ReserveCreatedEvent event = this.objectMapper.readValue(message, ReserveCreatedEvent.class);

        span.setTag("order.customer_id", event.getCustomerId());

        System.out.println(">>> Received payment confirmation event from payment service: " + event.getCustomerId());

        span.finish();
    }
}
