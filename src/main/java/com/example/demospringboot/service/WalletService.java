package com.example.demospringboot.service;

import com.example.demospringboot.model.ResponseModel;

import java.math.BigDecimal;

/**
 * Interface for Wallet related operations
 */
public interface WalletService {

    /**
     * Method for adding money to the wallet
     *
     * @param userId
     * @param amtToBeAdded
     * @return String
     */
    ResponseModel topup(int userId, BigDecimal amtToBeAdded);

    /**
     * Method for paying money through wallet
     *
     * @param userId
     * @param amtToBePaid
     * @return String
     */
    ResponseModel pay(int userId, BigDecimal amtToBePaid);

    /**
     * Method for refunding money to the wallet for a previous transaction
     *
     * @param userId
     * @param transactionId
     * @return String
     */
    ResponseModel refund(int userId, int transactionId);

    /**
     * Method for checking the wallet balance
     *
     * @param userId
     * @return String
     */
    String checkWalletBalance(int userId);

    /**
     * Method for Person to Person Payment through wallet
     *
     * @param fromUserId
     * @param amtToBePaid
     * @param toUserId
     * @return String
     */
    ResponseModel payP2P(int fromUserId, BigDecimal amtToBePaid, int toUserId);
}
