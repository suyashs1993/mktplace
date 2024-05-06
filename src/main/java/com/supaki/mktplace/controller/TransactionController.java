package com.supaki.mktplace.controller;
import com.supaki.mktplace.dto.TransactionInfo;
import com.supaki.mktplace.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path ="/payment")
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @PostMapping

    public ResponseEntity<String> processPayment(@RequestBody @Valid TransactionInfo transactionInfo) throws Exception {

            transactionService.processPayment(transactionInfo);
            return ResponseEntity.ok("Success");

    }
}
