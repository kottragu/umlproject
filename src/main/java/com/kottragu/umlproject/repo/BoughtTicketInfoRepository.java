package com.kottragu.umlproject.repo;

import com.kottragu.umlproject.model.BoughtTicketInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoughtTicketInfoRepository extends CrudRepository<BoughtTicketInfo, Long> {
}
