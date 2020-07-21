package com.example.demospringboot.controller;

import com.example.demospringboot.model.PassbookModel;
import com.example.demospringboot.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for Transaction
 */
@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/getPassbookDetails/{id}")
    public ResponseEntity<List<PassbookModel>> getPassbookDetailsForUser(@PathVariable int id) {

        return ResponseEntity.ok().body(transactionService.getPassbookTransactionsForUser(id));
    }
}
