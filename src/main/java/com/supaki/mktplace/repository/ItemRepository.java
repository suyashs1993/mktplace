package com.supaki.mktplace.repository;

import com.supaki.mktplace.entity.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ItemRepository extends CrudRepository<Item, Long> {

    Optional<Item> findByItemName(String itemName);
    List<Item> findByItemNameIn(Set<String> itemNames);

}
