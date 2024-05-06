package com.supaki.mktplace.repository;

import com.supaki.mktplace.entity.Listing;
import com.supaki.mktplace.enums.ListingStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListingRepository extends CrudRepository<Listing, Long> {
    List<Listing> findByStatusIn(List<ListingStatus> status);

}
