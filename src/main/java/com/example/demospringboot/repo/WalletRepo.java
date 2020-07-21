package com.example.demospringboot.repo;

import com.example.demospringboot.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

/**
 * Repository for Wallet related operations
 */
public interface WalletRepo extends JpaRepository<Wallet, Integer> {

    @Query(value = "SELECT * FROM wallet WHERE wallet.user_id=?1", nativeQuery = true)
    Wallet findWalletByUserId(int id);

    @Query(value = "SELECT wallet.amount FROM wallet where wallet.user_id=?1", nativeQuery = true)
    BigDecimal findWalletBalanceByUserId(int id);


}
