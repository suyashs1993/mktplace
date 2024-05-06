package com.supaki.mktplace.service;
import com.supaki.mktplace.dto.ListingInfo;;
import com.supaki.mktplace.entity.*;
import com.supaki.mktplace.enums.ListingStatus;
import com.supaki.mktplace.exception.ApiException;
import com.supaki.mktplace.repository.ListingRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ListingServiceTest {
    @Mock
    ItemService itemService;

    @Mock
    AccountItemService accountItemService;
   @Mock
    AccountService accountService;
    @Mock
    ListingRepository listingsRepository;
    @Mock
    TransactionService transactionService;

    @InjectMocks
    ListingService listingService;
    Account one;
    Account two;
    Account supaki;
    Listing listing;
    ListingInfo listingInfo;

    List<AccountItemMapping> accountItemMappings;

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

       listingInfo = ListingInfo.builder().sellerName("A").sellingPrice(new BigDecimal(50))
                .sellingQty(40).itemName("Item1").listingId(1l).build();
       AccountItemMapping accountItemMapping = new AccountItemMapping();
       Item item1 = new Item();
       item1.setItemName("Item1");

       accountItemMapping.setAccount(one);
       accountItemMapping.setItem(item1);
       accountItemMapping.setQuantity(40);
       accountItemMappings = new ArrayList<>();
       accountItemMappings.add(accountItemMapping);
    }

    @Test
    public void addListingTest() throws ApiException {
        when(accountService.getAccount("A")).thenReturn(one);
        when(accountItemService.getAllAccountItems(one.getId())).thenReturn(accountItemMappings);
        when(itemService.getItem("Item1")).thenReturn(new Item());
        when(transactionService.getTransactionByBuyerAndStartDateAfter(any(Long.class), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());
        listingService.addListing(listingInfo);
        verify(listingsRepository, times(1)).save(any(Listing.class));
    }
    @Test
    public void addListingExceptionTest() throws ApiException {
        when(accountService.getAccount("A")).thenReturn(one);
        when(accountItemService.getAllAccountItems(one.getId())).thenReturn(accountItemMappings);
        when(itemService.getItem("Item1")).thenReturn(new Item());
        when(transactionService.getTransactionByBuyerAndStartDateAfter(any(Long.class), any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(new Transaction()));
        Throwable exception = Assertions.assertThrows(ApiException.class,
                ()->listingService.addListing(listingInfo));
        verify(listingsRepository, times(0)).save(any(Listing.class));
    }

    @Test
    public void fetchAllListingTest(){
        Item item = new Item();
        item.setId(1l);
        item.setItemName("Item1");
        Listing listing = new Listing();
        listing.setSeller(one);
        listing.setItem(item);
        listing.setSellingPrice(new BigDecimal(50));
        listing.setSellingQuantity(1);
        listing.setId(1l);

        when(listingsRepository.findByStatusIn(new ArrayList<>(Arrays.asList(ListingStatus.LISTED, ListingStatus.PARTIALLY_SOLD)))
        ).thenReturn(Collections.singletonList(listing));

        List<ListingInfo> listingInfos = listingService.fetchAllListings("B");
        Assertions.assertTrue(listingInfos.size()==1);
        Assertions.assertTrue(listingInfos.get(0).getSellerName().equals("A"));

    }


}
