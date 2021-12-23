package com.kottragu.umlproject.repo;

import com.kottragu.umlproject.model.Purchase;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepository extends CrudRepository<Purchase, Long> {
    List<Purchase> findAllByOwnerId(Long ownerId);
}
