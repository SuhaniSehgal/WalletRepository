package com.example.demospringboot.service;

import com.example.demospringboot.model.PassbookModel;

import java.util.List;

/**
 * Interface for Transaction related operations
 */
public interface TransactionService {

    /**
     * Method for getting all the transactions for passbook details
     *
     * @param id
     * @return List<PassbookModel>
     */
    List<PassbookModel> getPassbookTransactionsForUser(int id);
}
