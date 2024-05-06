package com.supaki.mktplace.service;

import com.supaki.mktplace.constants.Constants;
import com.supaki.mktplace.dto.TransactionInfo;
import com.supaki.mktplace.entity.*;
import com.supaki.mktplace.enums.ListingStatus;
import com.supaki.mktplace.exception.ApiException;
import com.supaki.mktplace.repository.ListingRepository;
import com.supaki.mktplace.repository.TransactionRepository;
import com.supaki.mktplace.utillity.Utillity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class TransactionService {

    @Autowired
    AccountItemService accountItemService;
    @Autowired
    AccountService accountService;
    @Autowired
    ListingRepository listingsRepository;
    @Autowired
    TransactionRepository transactionRepository;


    @Transactional(rollbackFor = {SQLException.class, ApiException.class})
    public void processPayment(TransactionInfo transactionInfo) throws ApiException{
        Account buyer = accountService.getAccount(transactionInfo.getBuyerName());
        Account seller = accountService.getAccount(transactionInfo.getListing().getSellerName());

        List<Transaction> pastTxnInMonth = getTransactionByBuyerAndStartDateAfter(buyer.getId(),
                Utillity.getMonthBeforeTime());

        if(pastTxnInMonth.isEmpty()){
               settlePaymentAmount(transactionInfo, buyer, seller);
               accountItemService.updateItemQuantityForBuyerAndSeller(transactionInfo, buyer);
               Listing savedListing = updateListingStatus(transactionInfo);
               saveTransaction(transactionInfo, buyer, seller, savedListing);
        }else{
            throw new ApiException("Only 1 purchase is allowed per user in a month !");
        }
    }
    public void settlePaymentAmount(TransactionInfo transactionInfo, Account buyerAccount, Account sellerAccount) throws ApiException {
        Account companyAccount = accountService.getAccount(Constants.COMPANY);
        companyAccount.setAccountBalance(companyAccount.getAccountBalance().
                add(Constants.COMPANY_RATIO.multiply(transactionInfo.getPurchaseAmount())));
        sellerAccount.setAccountBalance(sellerAccount.getAccountBalance().
                add(Constants.SELLER_RATIO.multiply(transactionInfo.getPurchaseAmount())));
        List<Account> accounts = new ArrayList<>();
        accounts.add(sellerAccount);
        accounts.add(companyAccount);
        accountService.saveAccountList(accounts);
    }
    public List<Transaction> getTransactionByBuyerAndStartDateAfter(Long buyerId, LocalDateTime startTime){
        return transactionRepository.findByBuyerIdAndCreatedDateAfter(buyerId, startTime);
    }
    public void saveTransaction(TransactionInfo transactionInfo, Account buyerAccount, Account sellerAccount, Listing listing){
        Transaction transaction = new Transaction();
        transaction.setBuyer(buyerAccount);
        transaction.setSeller(sellerAccount);
        transaction.setPurchaseAmount(transactionInfo.getPurchaseAmount());
        transaction.setPurchaseQuantity(transactionInfo.getPurchaseQty());
        transaction.setListing(listing);
        transactionRepository.save(transaction);
    }

    public Listing updateListingStatus(TransactionInfo transactionInfo){
        Optional<Listing> listing = listingsRepository.findById(transactionInfo.getListing().getListingId());

        if(listing.get().getSellingQuantity().compareTo(transactionInfo.getPurchaseQty()) == 0){
            listing.get().setStatus(ListingStatus.SOLD);
        }else if(listing.get().getSellingQuantity().compareTo(transactionInfo.getPurchaseQty()) > 0){
            listing.get().setStatus(ListingStatus.PARTIALLY_SOLD);
        }
        listing.get().setSellingQuantity(listing.get().getSellingQuantity() - transactionInfo.getPurchaseQty());
        return listingsRepository.save(listing.get());
    }



}
