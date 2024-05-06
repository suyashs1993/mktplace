package com.supaki.mktplace.repository;

import com.supaki.mktplace.entity.AccountItemMapping;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountItemMappingRepository extends CrudRepository<AccountItemMapping, Long> {
    List<AccountItemMapping> findByAccountId(Long accountId);
}
