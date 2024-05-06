package com.supaki.mktplace.service;

import com.supaki.mktplace.dto.AccountInfo;
import com.supaki.mktplace.entity.Account;
import com.supaki.mktplace.enums.Gender;
import com.supaki.mktplace.exception.ApiException;
import com.supaki.mktplace.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    AccountRepository accountRepository;
    @Mock
    AccountItemService accountItemService;

    @InjectMocks
    AccountService accountService;

    Account one;
    Account two;

    AccountInfo account;

    @BeforeEach
    public void init() throws ApiException{
        one = new Account();
        one.setId(1l);
        one.setAccountName("A");
        one.setAccountBalance(new BigDecimal(0.0));
        two = new Account();
        two.setId(2L);
        two.setAccountName("B");
        two.setAccountBalance(new BigDecimal(1000.0));
        account = new AccountInfo("A",19, Gender.MALE,"IND",new BigDecimal(0));
    }
    @Test
    public void addAccountDetailsExceptionTest() throws ApiException {

        //AccountInfo account = new AccountInfo("A",19, Gender.MALE,"IND",new BigDecimal(0),null);
        when(accountRepository.findByAccountName("A")).thenReturn(Optional.of(one));
        Throwable ex = Assertions.assertThrows(ApiException.class, ()->accountService.addAccountDetails(account));

    }
    @Test
    public void addAccountDetailsTest() throws ApiException {

        //AccountInfo account = new AccountInfo("A",19, Gender.MALE,"IND",new BigDecimal(0),null);
        when(accountRepository.findByAccountName("A")).thenReturn(Optional.empty());
        accountService.addAccountDetails(account);
        verify(accountRepository, times(1)).save(any(Account.class));

    }
}
