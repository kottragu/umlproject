package com.kottragu.umlproject.repo;

import com.kottragu.umlproject.model.BookedTicketInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookedTicketRepository extends CrudRepository<BookedTicketInfo, Long> {
}
