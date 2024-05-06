package com.supaki.mktplace.controller;

import com.supaki.mktplace.dto.AccountInfo;
import com.supaki.mktplace.dto.AccountItemInfo;
import com.supaki.mktplace.dto.ItemInfo;
import com.supaki.mktplace.exception.ApiException;
import com.supaki.mktplace.service.AccountItemService;
import com.supaki.mktplace.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/account")
public class AccountController {
    @Autowired
    AccountService accountService;

    @PostMapping(path = "/create")
    public ResponseEntity<AccountInfo> addAccount(@RequestBody @Valid AccountInfo account) throws ApiException {
        return ResponseEntity.ok(accountService.addAccountDetails(account));
    }

    @GetMapping(path = "/get/{accountName}")
    public ResponseEntity<AccountInfo> getAccountDetails(@PathVariable String accountName) throws ApiException {
        return ResponseEntity.ok(accountService.getAccountDetails(accountName));
    }

    @PostMapping(path = "/addItems")
    public ResponseEntity<String> addAccountItems(@RequestBody @Valid AccountItemInfo accountItemInfo) throws ApiException {
        accountService.addAccountItems(accountItemInfo);
        return ResponseEntity.ok("Success");
    }

    @GetMapping(path = "/getItems/{accountName}")
    public ResponseEntity<List<AccountItemInfo.AccountItem>> getAccountItems(@PathVariable String accountName) throws ApiException {
        return ResponseEntity.ok(accountService.getAccountItems(accountName));
    }

}
