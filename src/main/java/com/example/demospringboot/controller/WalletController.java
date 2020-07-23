package com.example.demospringboot.controller;

import com.example.demospringboot.model.ResponseModel;
import com.example.demospringboot.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * Controller for Wallet
 */
@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping("/updateOnTopup/{id}/{amt}")
    public ResponseEntity<ResponseModel> updateWalletOnTopup(@PathVariable int id, @PathVariable BigDecimal amt) {

        return ResponseEntity.ok().body(walletService.topup(id, amt));
    }

    @PostMapping("/updateOnPay/{id}/{amt}")
    public ResponseEntity<ResponseModel> updateWalletOnPay(@PathVariable int id, @PathVariable BigDecimal amt) {

        return ResponseEntity.ok().body(walletService.pay(id, amt));
    }

    @PostMapping("/updateOnRefund/{id}/{transactionId}")
    public ResponseEntity<ResponseModel> updateWalletOnPay(@PathVariable int id, @PathVariable int transactionId) {

        return ResponseEntity.ok().body(walletService.refund(id, transactionId));
    }

    @PostMapping("/payP2P/{fromId}/{amt}/{toId}")
    public ResponseEntity<ResponseModel> updateWalletOnPay(@PathVariable int fromId, @PathVariable BigDecimal amt, @PathVariable int toId) {

        return ResponseEntity.ok().body(walletService.payP2P(fromId, amt, toId));
    }


    @GetMapping("/getBalance/{id}")
    public ResponseEntity<String> getWalletBalance(@PathVariable int id) {

        return ResponseEntity.ok().body(walletService.checkWalletBalance(id));
    }


}
