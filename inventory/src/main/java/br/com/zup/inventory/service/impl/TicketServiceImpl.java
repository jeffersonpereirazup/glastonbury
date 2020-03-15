package br.com.zup.inventory.service.impl;

import br.com.zup.inventory.controller.request.CreateTicketRequest;
import br.com.zup.inventory.controller.response.TicketResponse;
import br.com.zup.inventory.repository.TicketRepository;
import br.com.zup.inventory.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {

    private TicketRepository ticketRepository;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public String save(CreateTicketRequest request) {
        String ticketId = this.ticketRepository.save(request.toEntity()).getId();

        return ticketId;
    }

    @Override
    public List<TicketResponse> findAll() {
        return this.ticketRepository.findAll()
                .stream()
                .map(TicketResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
