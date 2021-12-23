package com.kottragu.umlproject.service;

import com.kottragu.umlproject.model.Status;
import com.kottragu.umlproject.model.Ticket;
import com.kottragu.umlproject.model.User;
import com.kottragu.umlproject.repo.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

@Service
public class TicketService {
    private TicketRepository ticketRepository;
    //TODO insert owner
    private Long ownerId = null;


    @Autowired
    public void setTicketRepository(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public List<Ticket> getAvailableTickets() {
        return ticketRepository.findAllByStatusAndDateAfter(Status.AVAILABLE, new GregorianCalendar());
    }


    public void save(Ticket ticket) {
        ticketRepository.save(ticket);
    }

    public List<Ticket> getBookedTickets() {
        return ticketRepository.findAllByOwnerIdAndStatusAndDateAfter(ownerId, Status.BOOKED, new GregorianCalendar());
    }

    public boolean bookTickets(Set<Ticket> tickets) {
        if (tickets.stream().allMatch(ticket -> ticket.getStatus().equals(Status.AVAILABLE))) {
            tickets.forEach(ticket -> {
                ticket.setStatus(Status.BOOKED);
                ticket.setOwnerId(ownerId);
            });
            ticketRepository.saveAll(tickets);
            return true;
        }
        return false;
    }

    public void cancelBook(Set<Ticket> tickets) {
        tickets.stream().forEach(ticket -> ticket.setStatus(Status.AVAILABLE));
        ticketRepository.saveAll(tickets);
    }
}
