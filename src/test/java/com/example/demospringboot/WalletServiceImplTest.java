package com.example.demospringboot;

import com.example.demospringboot.repo.WalletRepo;
import com.example.demospringboot.service.serviceImpl.WalletServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class WalletServiceImplTest {

    @InjectMocks
    WalletServiceImpl walletServiceImpl;

    @Mock
    WalletRepo walletRepo;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    final void testGetBalance() {

        BigDecimal mockBalance = BigDecimal.valueOf(100);
        when(walletRepo.findWalletBalanceByUserId(anyInt())).thenReturn(mockBalance);
        //fail("No implementations");
    }

}
