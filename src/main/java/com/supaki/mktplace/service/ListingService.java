package com.supaki.mktplace.service;

import com.supaki.mktplace.dto.ListingInfo;
import com.supaki.mktplace.entity.*;
import com.supaki.mktplace.enums.ListingStatus;
import com.supaki.mktplace.exception.ApiException;
import com.supaki.mktplace.repository.ListingRepository;
import com.supaki.mktplace.utillity.Utillity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ListingService {

    @Autowired
    ItemService itemService;

    @Autowired
    AccountService accountService;

    @Autowired
    ListingRepository listingsRepository;

    @Autowired
    TransactionService transactionService;

    @Autowired
    AccountItemService accountItemService;

    public void addListing(ListingInfo listingInfo) throws ApiException {

        Account sellerAccount = accountService.getAccount(listingInfo.getSellerName());
        checkIfListingPossible(listingInfo, sellerAccount);
        Item item  = itemService.getItem(listingInfo.getItemName());

        List<Transaction> pastTxnInDay = transactionService.getTransactionByBuyerAndStartDateAfter
                (sellerAccount.getId(), Utillity.getDayBeforeTime());

        if(pastTxnInDay.isEmpty()) {
                Listing listing = new Listing();
                listing.setSeller(sellerAccount);
                listing.setItem(item);
                listing.setSellingQuantity(listingInfo.getSellingQty());
                listing.setSellingPrice(listingInfo.getSellingPrice());
                listing.setStatus(ListingStatus.LISTED);
                listingsRepository.save(listing);
        }else{
           throw new ApiException("A Buyer can add a listing after 24 hours of buying an item!");
        }
    }

    public List<ListingInfo> fetchAllListings(String userName){

        List<ListingStatus> listingStatusList = new ArrayList<>(Arrays.asList(ListingStatus.LISTED, ListingStatus.PARTIALLY_SOLD));
        List<Listing> availableListings = listingsRepository.findByStatusIn(listingStatusList);

        List<ListingInfo> listingInfoList = availableListings.stream().map(l-> ListingInfo.builder().itemName(l.getItem().
                getItemName()).sellerName(l.getSeller().getAccountName()).itemId(l.getItem().getId())
                        .sellerId(l.getSeller().getId()).sellingQty(l.getSellingQuantity())
                        .sellingPrice(l.getSellingPrice()).listingId(l.getId()).build()).collect(Collectors.toList());

        listingInfoList = listingInfoList.stream().filter(l->!l.getSellerName().equals(userName)).collect(Collectors.toList());

        return listingInfoList;

    }

    public void checkIfListingPossible(ListingInfo listingInfo, Account account) throws ApiException {
        List<AccountItemMapping> accountItemMappings = accountItemService.getAllAccountItems(account.getId());
        Map<String, Integer> itemQtyMap = new HashMap<>();
        accountItemMappings.forEach(s->itemQtyMap.put(s.getItem().getItemName().toLowerCase(),s.getQuantity()));
        if(itemQtyMap.containsKey(listingInfo.getItemName().toLowerCase())){
            Integer availableQty = itemQtyMap.get(listingInfo.getItemName().toLowerCase());
            if(availableQty < listingInfo.getSellingQty()){
                throw new ApiException("You cannot list more than "+availableQty+ " units of item "+listingInfo.getItemName());
            }
        }else{
            throw new ApiException("You can't list item "+listingInfo.getItemName() + " as you don't have it!");
        }
    }

    public Listing saveListing(Listing listing){
        listingsRepository.save(listing);
        return listing;
    }
}
