package com.supaki.mktplace.controller;

import com.supaki.mktplace.dto.ItemInfo;
import com.supaki.mktplace.exception.ApiException;
import com.supaki.mktplace.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path ="/item")
public class ItemController {

    @Autowired
    ItemService itemService;

    @PostMapping(path = "/create")
    public ResponseEntity<ItemInfo> addItem(@RequestBody @Valid ItemInfo itemInfo) throws ApiException {
        return ResponseEntity.ok(itemService.addItem(itemInfo));

    }

    @PostMapping(path = "/createList")
    public ResponseEntity<List<ItemInfo>> addItems(@RequestBody @Valid List<ItemInfo> itemInfo) throws ApiException {
        List<ItemInfo> savedItems = itemService.addItemList(itemInfo);
        return ResponseEntity.ok(savedItems);
    }


    @GetMapping(path = "/get/{itemName}")
    public ResponseEntity<ItemInfo> getItem(@PathVariable String itemName) throws ApiException {
        return ResponseEntity.ok(itemService.getItemInfo(itemName));
    }

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<ItemInfo>> getItems() throws ApiException {;
        return ResponseEntity.ok(itemService.getAllItems());
    }
}
