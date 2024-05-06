package com.supaki.mktplace.controller;

import com.supaki.mktplace.dto.ListingInfo;
import com.supaki.mktplace.exception.ApiException;
import com.supaki.mktplace.service.ListingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path ="/listing")
public class ListingController {

    @Autowired
    ListingService listingService;



    @PostMapping(path ="/add")
    public ResponseEntity<String> addListing(@RequestBody @Valid ListingInfo listingInfo) throws ApiException {
        listingService.addListing(listingInfo);
        return ResponseEntity.ok("Success");
    }


    @GetMapping(path ="/get/{accountName}")
    public ResponseEntity<List<ListingInfo>> fetchAllListings(@PathVariable String accountName){
        return ResponseEntity.ok(listingService.fetchAllListings(accountName));
    }
}
