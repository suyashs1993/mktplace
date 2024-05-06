package com.supaki.mktplace.service;

import com.supaki.mktplace.constants.Constants;
import com.supaki.mktplace.dto.AccountInfo;
import com.supaki.mktplace.dto.AccountItemInfo;
import com.supaki.mktplace.dto.ItemInfo;
import com.supaki.mktplace.entity.Account;
import com.supaki.mktplace.enums.AccountType;
import com.supaki.mktplace.exception.ApiException;
import com.supaki.mktplace.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AccountItemService accountItemService;


    public AccountInfo getAccountDetails(String accountName) throws ApiException {
       Account account = getAccount(accountName);
       AccountInfo accountInfo = new AccountInfo(account.getAccountName(), account.getAge(),
               account.getGender(), account.getCountryCode(),account.getAccountBalance());
       return accountInfo;
    }

    public List<AccountItemInfo.AccountItem> getAccountItems(String accountName) throws ApiException {
        Account account = getAccount(accountName);
        List<AccountItemInfo.AccountItem> items = accountItemService.getAccountItems(account.getId());
        return items;
    }

    public Account getAccount(String accountName) throws ApiException {
        Optional<Account> accountOptional = accountRepository.findByAccountName(accountName);
        if(!accountOptional.isPresent()){
            throw new ApiException("Account not found for account name:- "+accountName);
        }
        return accountOptional.get();
    }

    public void checkIfAccountExist(String accountName) throws ApiException {
        Optional<Account> accountOptional = accountRepository.findByAccountName(accountName);
        if(accountOptional.isPresent()){
            throw new ApiException("Account already exists for account name:- "+accountName);
        }
    }


    public AccountInfo addAccountDetails(AccountInfo accountDetails) throws ApiException {
        checkIfAccountExist(accountDetails.getAccountName());
        Account account = new Account();
        account.setAccountName(accountDetails.getAccountName());
        account.setAge(accountDetails.getAge());
        account.setGender(accountDetails.getGender());
        account.setCountryCode(account.getCountryCode());
        account.setAccountBalance(accountDetails.getAccountBalance());
        saveAccount(account);
        return accountDetails;
       // accountItemService.addAccountItems(account, accountDetails.getItems());
    }

    public void addAccountItems(AccountItemInfo accountItemInfo) throws ApiException {
        Account account = getAccount(accountItemInfo.getAccountName());
        accountItemService.addAccountItems(account, accountItemInfo.getItems());
    }

    @EventListener(ApplicationReadyEvent.class)
    public void insertCompanyAccount(){
        Optional<Account> companyAccount = accountRepository.findByAccountName(Constants.COMPANY);
        if(!companyAccount.isPresent()){
            Account account = new Account();
            account.setAccountName(Constants.COMPANY);
            account.setAccountType(AccountType.COMPANY);
            account.setAccountBalance(new BigDecimal(0));
            accountRepository.save(account);
        }
    }
    public void saveAccount(Account account){
        accountRepository.save(account);
    }
    public void saveAccountList(List<Account> accounts){
        accountRepository.saveAll(accounts);
    }

}
