package com.kottragu.umlproject.repo;

import com.kottragu.umlproject.model.Status;
import com.kottragu.umlproject.model.Ticket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Calendar;
import java.util.List;

@Repository
public interface TicketRepository extends CrudRepository<Ticket, Long> {
    List<Ticket> findAllByStatusAndDateAfter(Status status, Calendar date);
    List<Ticket> findAllByOwnerIdAndStatusAndDateAfter(Long ownerId, Status status, Calendar date);
}
