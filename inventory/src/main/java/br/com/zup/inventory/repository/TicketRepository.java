package br.com.zup.inventory.repository;

import br.com.zup.inventory.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, String> {

}
