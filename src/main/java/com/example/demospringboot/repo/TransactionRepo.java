package com.example.demospringboot.repo;

import com.example.demospringboot.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository for Transaction related operations
 */
public interface TransactionRepo extends JpaRepository<Transaction, Integer> {

    @Query(value = "SELECT * from transaction where payer=?1", nativeQuery = true)
    List<Transaction> getAllTransactionsForUserPassbook(int id);

    @Query(value = "SELECT * FROM transaction where transaction_id=?1", nativeQuery = true)
    Transaction getTransactionDetailsForTransactionId(int transactionId);

    @Query(value = "SELECT * from transaction where transaction_no=?1", nativeQuery = true)
    Transaction findTransactionByTxnNo(String txnNo);
}
