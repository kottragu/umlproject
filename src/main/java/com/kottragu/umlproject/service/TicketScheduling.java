package com.kottragu.umlproject.service;

import com.kottragu.umlproject.model.Status;
import com.kottragu.umlproject.model.Ticket;
import com.kottragu.umlproject.model.TimetableTicket;
import com.kottragu.umlproject.repo.TicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class TicketScheduling {
    private List<TimetableTicket> tickets = new ArrayList<>();
    private Calendar today = new GregorianCalendar();
    /*private List<TicketRepository> repository;*/

    TicketScheduling() {
       TimetableTicket ticket = new TimetableTicket();
       ticket.setFrequency(3);
       ticket.setDirectionFrom("Moscow");
       ticket.setDirectionTo("Saint Petersburg");
       ticket.setPrice(15000);
       Calendar calendar = new GregorianCalendar();
       calendar.set(2021,Calendar.DECEMBER,20, 15, 40);
       ticket.setStartDate(calendar);
       tickets.add(ticket);
    }


    @Scheduled(cron = "0 15 * * * *")
    public void schedule() {
        log.info("Schedule started!");
        for (TimetableTicket ticket: tickets) {
            if (amplitudeBetweenDates(ticket.getStartDate(), today) % ticket.getFrequency() == 0) {
                log.info(createTicket(ticket).toString());
            }
        }
    }

    private Ticket createTicket(TimetableTicket timetableTicket) {
        Ticket ticket = new Ticket();

        ticket.setDirectionFrom(timetableTicket.getDirectionFrom());
        ticket.setDirectionTo(timetableTicket.getDirectionTo());
        ticket.setPrice(timetableTicket.getPrice());
        ticket.setDate(createTodayCalendar(timetableTicket));
        ticket.setStatus(Status.AVAILABLE);
        return ticket;
    }

    private Calendar createTodayCalendar(TimetableTicket timetableTicket) {
        Calendar day = new GregorianCalendar();
        day.set(
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH),
                timetableTicket.getStartDate().get(Calendar.HOUR_OF_DAY),
                timetableTicket.getStartDate().get(Calendar.MINUTE)
        );
        return day;
    }

    private long amplitudeBetweenDates(Calendar start, Calendar end) {
        return TimeUnit.MILLISECONDS.toDays(end.getTimeInMillis() - start.getTimeInMillis()) + 1;
    }
}
