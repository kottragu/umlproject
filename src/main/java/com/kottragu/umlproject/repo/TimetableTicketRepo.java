package com.kottragu.umlproject.repo;

import com.kottragu.umlproject.model.TimetableTicket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimetableTicketRepo extends CrudRepository<TimetableTicket, Long> {
}
