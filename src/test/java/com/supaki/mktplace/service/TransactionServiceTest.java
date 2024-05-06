package com.supaki.mktplace.service;
import com.supaki.mktplace.dto.ListingInfo;
import com.supaki.mktplace.dto.TransactionInfo;
import com.supaki.mktplace.entity.Account;
import com.supaki.mktplace.entity.Listing;
import com.supaki.mktplace.entity.Transaction;
import com.supaki.mktplace.enums.ListingStatus;
import com.supaki.mktplace.exception.ApiException;
import com.supaki.mktplace.repository.ListingRepository;
import com.supaki.mktplace.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    AccountService accountService;
    @Mock
    ListingRepository listingRepository;
    @Mock
    AccountItemService accountItemService;
    @Mock
    TransactionRepository transactionRepository;
    @InjectMocks
    TransactionService transactionService;
    TransactionInfo transactionInfo;
    Account one;
    Account two;
    Account supaki;
    Listing listing;

    @BeforeEach
    public void init() throws ApiException{
        one = new Account();
        one.setId(1l);
        one.setAccountName("A");
        one.setAccountBalance(new BigDecimal(1000.0));
        two = new Account();
        two.setId(2L);
        two.setAccountName("B");
        two.setAccountBalance(new BigDecimal(1000.0));
        supaki = new Account();
        supaki.setAccountName("SUPAKI");
        supaki.setAccountBalance(new BigDecimal(0.0));
        ListingInfo listingInfo = ListingInfo.builder().sellerName("A").sellingPrice(new BigDecimal(50))
                .sellingQty(40).itemName("Item1").listingId(1l).build();

        transactionInfo = new TransactionInfo("B",1,new BigDecimal(50),listingInfo);
    }

    @Test
    public void settlePaymentTest() throws ApiException {
        when(accountService.getAccount("SUPAKI")).thenReturn(supaki);
        transactionService.settlePaymentAmount(transactionInfo, two, one);
        Assertions.assertTrue(one.getAccountBalance().intValue() == 1045);
        Assertions.assertTrue(two.getAccountBalance().intValue() == 1000);
        Assertions.assertTrue(supaki.getAccountBalance().intValue() == 5);
    }

    @Test
    public void updateListingStatusTest() throws ApiException {
        Listing listing = new Listing();
        listing.setId(1l);
        listing.setSellingPrice(new BigDecimal(50));
        listing.setSellingQuantity(40);
        Assertions.assertTrue(listing.getStatus().equals(ListingStatus.LISTED));
        when(listingRepository.findById(1l)).thenReturn(Optional.of(listing));
        transactionService.updateListingStatus(transactionInfo);
        Assertions.assertTrue(listing.getStatus().equals(ListingStatus.PARTIALLY_SOLD));
        Assertions.assertTrue(listing.getSellingQuantity().equals(39));
    }

    @Test
    public void processPaymentPassTest() throws ApiException {
        Listing listing = new Listing();
        listing.setId(1l);
        listing.setSellingPrice(new BigDecimal(50));
        listing.setSellingQuantity(40);
        when(accountService.getAccount("A")).thenReturn(one);
        when(accountService.getAccount("B")).thenReturn(two);
        when(accountService.getAccount("SUPAKI")).thenReturn(supaki);
        when(transactionRepository.findByBuyerIdAndCreatedDateAfter(any(Long.class), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());
        when(listingRepository.findById(1l)).thenReturn(Optional.of(listing));

        transactionService.processPayment(transactionInfo);
        verify(accountService, times(3)).getAccount(any(String.class));
        verify(listingRepository, times(1)).findById(1l);
        verify(transactionRepository, times(1)).save(any(Transaction.class));

    }
    @Test
    public void processPaymentThrowExceptionTest() throws ApiException {
        when(accountService.getAccount("A")).thenReturn(one);
        when(accountService.getAccount("B")).thenReturn(two);

        when(transactionRepository.findByBuyerIdAndCreatedDateAfter(any(Long.class), any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(new Transaction()));

        Throwable exception = Assertions.assertThrows(ApiException.class,
                ()-> transactionService.processPayment(transactionInfo));

    }

}
