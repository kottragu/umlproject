package com.kottragu.umlproject.service;

import com.kottragu.umlproject.model.Status;
import com.kottragu.umlproject.model.Ticket;
import com.kottragu.umlproject.model.TimetableTicket;
import com.kottragu.umlproject.repo.TicketRepository;
import com.kottragu.umlproject.repo.TimetableTicketRepo;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@NoArgsConstructor
public class TicketScheduling {
    private TimetableTicketRepo timetableTicketRepo;
    private TicketRepository ticketRepository;
    private int createTime = 40;

    @Autowired
    TicketScheduling(TimetableTicketRepo timetableTicketRepo, TicketRepository ticketRepository) {
        this.timetableTicketRepo = timetableTicketRepo;
        this.ticketRepository = ticketRepository;

    }

    public void addTimetable(TimetableTicket ticket) {
        timetableTicketRepo.save(ticket);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void schedule() {
        Calendar today = new GregorianCalendar();
        log.info("Schedule started!");
        for (TimetableTicket timeTableTicket: timetableTicketRepo.findAll()) {
            if (amplitudeBetweenDates(timeTableTicket.getStartDate(), today) % timeTableTicket.getFrequency() == 0) {
                ticketRepository.save(createTicket(timeTableTicket));
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
                day.get(Calendar.YEAR),
                day.get(Calendar.MONTH),
                day.get(Calendar.DAY_OF_MONTH),
                timetableTicket.getStartDate().get(Calendar.HOUR_OF_DAY),
                timetableTicket.getStartDate().get(Calendar.MINUTE)
        );
        day.add(Calendar.DATE, createTime);
        return day;
    }

    private long amplitudeBetweenDates(Calendar start, Calendar end) {
        end.add(Calendar.DATE, createTime);
        return TimeUnit.MILLISECONDS.toDays(end.getTimeInMillis() - start.getTimeInMillis()) + 1;
    }
}
