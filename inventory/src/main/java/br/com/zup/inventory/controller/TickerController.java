package br.com.zup.inventory.controller;

import br.com.zup.inventory.controller.request.CreateTicketRequest;
import br.com.zup.inventory.controller.response.TicketResponse;
import br.com.zup.inventory.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sun.font.CreatedFontTracker;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TickerController {

    private TicketService ticketService;

    @Autowired
    public TickerController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public String Post(@RequestBody CreateTicketRequest request){
        return this.ticketService.save(request);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<TicketResponse> Get(){
        return  this.ticketService.findAll();
    }
}
