package br.com.zup.payment.service;

import br.com.zup.payment.event.ReserveCreatedEvent;

public interface Payment {

    void confirmOrder(ReserveCreatedEvent event);
}
