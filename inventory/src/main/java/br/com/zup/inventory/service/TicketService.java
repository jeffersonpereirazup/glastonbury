package br.com.zup.inventory.service;

import br.com.zup.inventory.controller.request.CreateTicketRequest;
import br.com.zup.inventory.controller.response.TicketResponse;

import java.util.List;

public interface TicketService {

    String save(CreateTicketRequest request);

    List<TicketResponse> findAll();

}
