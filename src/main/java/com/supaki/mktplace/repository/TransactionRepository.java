package com.supaki.mktplace.repository;
import com.supaki.mktplace.entity.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    List<Transaction> findByBuyerIdAndCreatedDateAfter(Long buyerId, LocalDateTime listingDate);
}
