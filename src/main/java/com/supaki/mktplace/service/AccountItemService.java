package com.supaki.mktplace.service;

import com.supaki.mktplace.dto.AccountItemInfo;
import com.supaki.mktplace.dto.ItemInfo;
import com.supaki.mktplace.dto.TransactionInfo;
import com.supaki.mktplace.entity.Account;
import com.supaki.mktplace.entity.Item;
import com.supaki.mktplace.entity.AccountItemMapping;
import com.supaki.mktplace.repository.AccountItemMappingRepository;
import com.supaki.mktplace.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AccountItemService {

    @Autowired
    AccountItemMappingRepository accountItemMappingRepository;

    @Autowired
    ItemService itemService;

    public void updateItemQuantityForBuyerAndSeller(TransactionInfo transactionInfo, Account buyer){

        List<AccountItemMapping> sellerAccountItemMappings = accountItemMappingRepository.
                findByAccountId(transactionInfo.getListing().getSellerId());
        List<AccountItemMapping> buyerAccountItemMappings = accountItemMappingRepository.
                findByAccountId(buyer.getId());

        boolean itemPresent = false;

        for(AccountItemMapping buyerItemAccount : buyerAccountItemMappings){
            if(buyerItemAccount.getItem().getId().equals(transactionInfo.getListing().getItemId())){
                buyerItemAccount.setQuantity(buyerItemAccount.getQuantity() + transactionInfo.getPurchaseQty());
                itemPresent = true;
                break;
            }
        }
        if(!itemPresent){
            Item item = itemService.getItemById(transactionInfo.getListing().getItemId());
            AccountItemMapping purchasedAccountItemMapping = new AccountItemMapping();
            purchasedAccountItemMapping.setItem(item);
            purchasedAccountItemMapping.setAccount(buyer);
            purchasedAccountItemMapping.setQuantity(transactionInfo.getPurchaseQty());
            buyerAccountItemMappings.add(purchasedAccountItemMapping);
        }

        for(AccountItemMapping sellerItemAccount : sellerAccountItemMappings){
            if(sellerItemAccount.getItem().getId().equals(transactionInfo.getListing().getItemId())){
                sellerItemAccount.setQuantity(sellerItemAccount.getQuantity() - transactionInfo.getPurchaseQty());
                break;
            }
        }
        buyerAccountItemMappings.addAll(sellerAccountItemMappings);
        accountItemMappingRepository.saveAll(buyerAccountItemMappings);
    }

    public List<AccountItemInfo.AccountItem> getAccountItems(Long accountId){
        List<AccountItemMapping> accountItemMappings = getAllAccountItems(accountId);;
        List<AccountItemInfo.AccountItem> accountItemList = new ArrayList<>();
        for(AccountItemMapping accountItemMapping : accountItemMappings) {
            if(accountItemMapping.getQuantity() > 0) {
                AccountItemInfo.AccountItem accountItem = new AccountItemInfo.AccountItem(accountItemMapping
                        .getItem().getItemName(), accountItemMapping.getQuantity());
                accountItemList.add(accountItem);
            }
        }
        return accountItemList;
    }


    public void addAccountItems(Account account, List<AccountItemInfo.AccountItem> accountItems) {
        List<AccountItemMapping> accountItemMappings = new ArrayList<>();
        Map<String, Integer> itemQtyMap = new HashMap<>();
        Map<String, AccountItemMapping> itemNameAccntItemMap = new HashMap<>();

        if(!CollectionUtils.isEmpty(accountItems)) {
            accountItems.forEach(s -> itemQtyMap.put(s.getItemName().toLowerCase(), s.getQuantity()));
            Set<String> itemNames = itemQtyMap.keySet();
            List<Item> presentItems = itemService.fetchExistingItems(itemNames);
            List<AccountItemMapping> existingAccountItemMappings = getAllAccountItems(account.getId());
            existingAccountItemMappings.forEach(s->itemNameAccntItemMap.put(s.getItem().getItemName().toLowerCase(), s));
            for (Item item : presentItems) {
                Integer qty = itemQtyMap.get(item.getItemName().toLowerCase());
                if(itemNameAccntItemMap.containsKey(item.getItemName().toLowerCase())){
                    AccountItemMapping existingMapping = itemNameAccntItemMap.get(item.getItemName().toLowerCase());
                    existingMapping.setQuantity(existingMapping.getQuantity()+qty);
                    accountItemMappings.add(existingMapping);
                }else {
                    AccountItemMapping accountItemMapping = new AccountItemMapping();
                    accountItemMapping.setItem(item);
                    accountItemMapping.setAccount(account);
                    accountItemMapping.setQuantity(qty);
                    accountItemMappings.add(accountItemMapping);
                }
            }
            accountItemMappingRepository.saveAll(accountItemMappings);
        }
    }

    public List<AccountItemMapping> getAllAccountItems(Long accountId){
        return accountItemMappingRepository.findByAccountId(accountId);
    }
}
