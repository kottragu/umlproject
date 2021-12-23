package com.kottragu.umlproject.service;

import com.kottragu.umlproject.model.*;
import com.kottragu.umlproject.repo.PurchaseRepository;
import com.kottragu.umlproject.repo.TicketRepository;
import com.kottragu.umlproject.repo.UserRepository;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@NoArgsConstructor
public class PurchaseService {
    private PurchaseRepository purchaseRepository;
    private PayService payService;
    private User owner;
    private TicketRepository ticketRepository;
    private UserRepository userRepository;

    public void setData() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        owner = userRepository.findUserByUsername(userDetails.getUsername());
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPayService(PayService payService) {
        this.payService = payService;
    }

    @Autowired
    public void setTicketRepository(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Autowired
    public void setPurchaseRepository(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    public List<Purchase> getPurchases() {
        return purchaseRepository.findAllByOwnerId(owner.getId());
    }

    public boolean createPurchase(Set<Ticket> tickets, Card card) {
        if (//payService.pay(card, calculateTotalCost(tickets)) && - опция русской рулетки
                tickets.stream().allMatch(ticket ->
                        ticket.getStatus().equals(Status.AVAILABLE) ||
                        ticket.getStatus().equals(Status.BOOKED) && ticket.getOwnerId().equals(owner.getId()))) {
            for (Ticket ticket : tickets) {
                ticket.setStatus(Status.BOUGHT);
            }
            ticketRepository.saveAll(tickets);
            Purchase purchase = new Purchase();
            purchase.setDate(new GregorianCalendar());
            purchase.setOwnerId(owner.getId());
            List<Ticket> list = new ArrayList<>(tickets);
            purchase.setTickets(list);
            purchase.setTotalCost(calculateTotalCost(tickets));
            purchaseRepository.save(purchase);
            return true;
        }
        return false;
    }

    private double calculateTotalCost(Set<Ticket> tickets) {
        return tickets.stream().mapToDouble(Ticket::getPrice).sum();
    }
}
