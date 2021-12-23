package com.kottragu.umlproject.service;

import com.kottragu.umlproject.model.Status;
import com.kottragu.umlproject.model.Ticket;
import com.kottragu.umlproject.model.User;
import com.kottragu.umlproject.repo.TicketRepository;
import com.kottragu.umlproject.repo.UserRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

@Service
@NoArgsConstructor
public class TicketService {
    private TicketRepository ticketRepository;
    //TODO insert owner
    private User owner;
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setData() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        owner = userRepository.findUserByUsername(userDetails.getUsername());
    }

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
        return ticketRepository.findAllByOwnerIdAndStatusAndDateAfter(owner.getId(), Status.BOOKED, new GregorianCalendar());
    }

    public boolean bookTickets(Set<Ticket> tickets) {
        if (tickets.stream().allMatch(ticket -> ticket.getStatus().equals(Status.AVAILABLE))) {
            tickets.forEach(ticket -> {
                ticket.setStatus(Status.BOOKED);
                ticket.setOwnerId(owner.getId());
            });
            ticketRepository.saveAll(tickets);
            return true;
        }
        return false;
    }

    public void cancelBook(Set<Ticket> tickets) {
        tickets.forEach(ticket -> ticket.setStatus(Status.AVAILABLE));
        ticketRepository.saveAll(tickets);
    }
}
