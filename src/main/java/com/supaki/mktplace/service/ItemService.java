package com.supaki.mktplace.service;


import com.supaki.mktplace.dto.AccountItemInfo;
import com.supaki.mktplace.dto.ItemInfo;
import com.supaki.mktplace.entity.Item;
import com.supaki.mktplace.exception.ApiException;
import com.supaki.mktplace.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ItemService {

    @Autowired
    ItemRepository itemRepository;


    public ItemInfo addItem(ItemInfo itemInfo) throws ApiException {
        checkIfItemExist(itemInfo.getItemName());
        Item item = new Item();
        item.setItemCategory(itemInfo.getItemCategory());
        item.setItemName(itemInfo.getItemName());
        item.setItemPrice(itemInfo.getItemPrice());
        item.setItemDescription(itemInfo.getItemDescription());
        itemRepository.save(item);
        return itemInfo;
    }

    public List<ItemInfo> addItemList(List<ItemInfo> itemInfoList) throws ApiException {
        Set<String> itemNames = itemInfoList.stream().map(s -> s.getItemName()).collect(Collectors.toSet());
        List<Item> existingItems = fetchExistingItems(itemNames);
        Set<String> existingItemNames = existingItems.stream().map(s -> s.getItemName().toLowerCase()).collect(Collectors.toSet());
        List<Item> newItems = new ArrayList<>();
        for (ItemInfo itemInfo : itemInfoList) {
            if (!existingItemNames.contains(itemInfo.getItemName().toLowerCase())) {
                Item item = new Item();
                item.setItemCategory(itemInfo.getItemCategory());
                item.setItemName(itemInfo.getItemName());
                item.setItemPrice(itemInfo.getItemPrice());
                item.setItemDescription(itemInfo.getItemDescription());
                newItems.add(item);
            }
        }
        itemRepository.saveAll(newItems);
        itemInfoList = itemInfoList.stream().filter(s->!existingItemNames.contains(s.getItemName().toLowerCase())).collect(Collectors.toList());
        return itemInfoList;
    }

    public void checkIfItemExist(String itemName) throws ApiException {
        Optional<Item> itemOptional = itemRepository.findByItemName(itemName);
        if(itemOptional.isPresent()){
            throw new ApiException("Item already exists!");
        }
    }

    public ItemInfo getItemInfo(String itemName) throws ApiException {
        Item item = getItem(itemName);
        return new ItemInfo(itemName,item.getItemPrice(), item.getItemDescription(), item.getItemCategory());
    }

    public List<ItemInfo> getAllItems(){
        Iterable<Item> items = itemRepository.findAll();
        List<ItemInfo> itemInfoList = new ArrayList<>();
        for(Item item : items){
            itemInfoList.add(new ItemInfo(item.getItemName(), item.getItemPrice(),
                    item.getItemDescription(), item.getItemCategory()));
        }
        return itemInfoList;
    }


    public Item getItem(String itemName) throws ApiException {
        Optional<Item> itemOptional = itemRepository.findByItemName(itemName);
        if(!itemOptional.isPresent()){
            throw new ApiException("Item not found !");
        }
        return itemOptional.get();
    }

    public Item getItemById(Long id){
        Optional<Item> itemOptional = itemRepository.findById(id);
        if(!itemOptional.isPresent()){
            throw new RuntimeException("Item not found !");
        }
        return itemOptional.get();
    }


    public List<Item> fetchExistingItems(Set<String> itemNames){
        return itemRepository.findByItemNameIn(itemNames);
    }

}
