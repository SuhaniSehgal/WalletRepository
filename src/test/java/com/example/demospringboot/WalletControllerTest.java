package com.example.demospringboot;

import com.example.demospringboot.controller.WalletController;
import com.example.demospringboot.service.serviceImpl.WalletServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class WalletControllerTest {

    @InjectMocks
    WalletController walletController;

    @Mock
    WalletServiceImpl walletService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    final void getWalletBalanceForUser() {
        when(walletService.checkWalletBalance(anyInt())).thenReturn(String.valueOf(new BigDecimal(100)));

        ResponseEntity<String> walletBalance = walletController.getWalletBalance(1);

        assertNotNull(walletBalance);
    }

}
