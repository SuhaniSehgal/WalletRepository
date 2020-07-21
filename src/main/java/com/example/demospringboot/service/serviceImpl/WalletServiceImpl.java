package com.example.demospringboot.service.serviceImpl;

import com.example.demospringboot.entity.Transaction;
import com.example.demospringboot.entity.User;
import com.example.demospringboot.entity.Wallet;
import com.example.demospringboot.repo.TransactionRepo;
import com.example.demospringboot.repo.UserRepo;
import com.example.demospringboot.repo.WalletRepo;
import com.example.demospringboot.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;


/**
 * This is the Implementation for the WalletService
 */
@Service
@Transactional
public class WalletServiceImpl implements WalletService {

    //format for currency to be displayed to the user
    NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
    @Autowired
    private WalletRepo walletRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private TransactionRepo transactionRepo;

    @Override
    public String topup(int userId, BigDecimal amtToBeAdded) {
        Optional<Wallet> walletOptionalData = Optional.ofNullable(walletRepo.findWalletByUserId(userId));
        Optional<User> user = userRepo.findById(userId);


        if (user.isPresent()) {
            //check whether the user is in active state
            if (user.get().getUserStatus() != 1) {

                //check if wallet details already exist for a user, then add the amt to the existing wallet amt
                if (walletOptionalData.isPresent()) {
                    Wallet wallet = walletOptionalData.get();
                    BigDecimal finalAmt = wallet.getAmount().add(amtToBeAdded);
                    wallet.setAmount(finalAmt);
                    String transId = "XX01" + UUID.randomUUID().toString();

                    Transaction transaction = new Transaction();
                    transaction.setUser(user.get());
                    transaction.setTransactionNo(transId);
                    transaction.setTransactionDateTime(new Date());
                    transaction.setTransactionAmt(amtToBeAdded);
                    transaction.setStatus("Credit");
                    transaction.setClosingBalance(finalAmt);
                    transactionRepo.save(transaction);
                    walletRepo.save(wallet);

                    return format.format(amtToBeAdded) + " has been added to your wallet Successfully with transaction no : " + transId + " at " +
                            transaction.getTransactionDateTime() + ".\nYour closing balance is : " + format.format(transaction.getClosingBalance());
                }

                //else create a new wallet with the amt requested for topup
                else {
                    Wallet wallet = new Wallet();
                    wallet.setAmount(amtToBeAdded);

                    //check if the user exists
                    if (user.isPresent()) {
                        wallet.setUser(user.get());
                        String transId = "XX01" + UUID.randomUUID().toString();

                        Transaction transaction = new Transaction();
                        transaction.setUser(user.get());
                        transaction.setTransactionNo(transId);
                        transaction.setTransactionDateTime(new Date());
                        transaction.setTransactionAmt(amtToBeAdded);
                        transaction.setStatus("Credit");
                        transaction.setClosingBalance(amtToBeAdded);
                        transactionRepo.save(transaction);
                        walletRepo.save(wallet);
                        return format.format(amtToBeAdded) + " has been added to your wallet Successfully with transaction no : " + transId + " at " +
                                transaction.getTransactionDateTime() + ".\nYour closing balance is : " +
                                format.format(transaction.getClosingBalance());

                    }
                    //if user doesn't exist, display message
                    else {
                        return "No user exists with the given id";
                    }

                }
            }
            //if user inactive, display message to the user
            else {
                return "User Inactive. Cannot make transaction!";
            }

        } else {
            return "User doesn't exist!";
        }


    }

    @Override
    public String pay(int userId, BigDecimal amtToBePaid) {

        Optional<Wallet> walletOptionalData = Optional.ofNullable(walletRepo.findWalletByUserId(userId));
        Optional<User> user = userRepo.findById(userId);

        if (user.isPresent()) {
            //check if user is active
            if (user.get().getUserStatus() != 1) {

                //check if wallet details for the user exists
                if (walletOptionalData.isPresent()) {
                    Wallet wallet = walletOptionalData.get();
                    BigDecimal walletBalance = wallet.getAmount();

                    //check if the user has sufficient balance in the wallet to pay certain amt
                    if (walletBalance.compareTo(amtToBePaid) == 1) {
                        BigDecimal finalAmt = walletBalance.subtract(amtToBePaid);
                        wallet.setAmount(finalAmt);
                        String transId = "XX02" + UUID.randomUUID().toString();

                        Transaction transaction = new Transaction();
                        transaction.setUser(user.get());
                        transaction.setTransactionNo(transId);
                        transaction.setTransactionDateTime(new Date());
                        transaction.setStatus("Debit");
                        transaction.setClosingBalance(finalAmt);
                        transaction.setTransactionAmt(amtToBePaid);
                        transactionRepo.save(transaction);
                        walletRepo.save(wallet);

                        return format.format(amtToBePaid) + " amount has been successfully paid from your wallet against transaction no : " + transId + " at " +
                                transaction.getTransactionDateTime() + ".\nYour closing balance is : " + format.format(transaction.getClosingBalance());
                    }
                    //else display message for insufficient amt
                    else {
                        return "No sufficient balance in Wallet!";
                    }
                }
                //else display message for no wallet amount
                else {
                    return "No Amount present in Wallet!";
                }
            }
            //if user is inactive, display message
            else {
                return "User Inactive. Cannot make transaction!";
            }
        } else {
            return "User doesn't exist!";
        }

    }

    @Override
    public String refund(int userId, int transactionId) {
        Transaction transaction = transactionRepo.getTransactionDetailsForTransactionId(transactionId);
        Optional<User> user = userRepo.findById(userId);

        if (user.isPresent()) {
            //check is the user is active
            if (user.get().getUserStatus() != 1) {

                //Validate the user id and status as debit
                if (transaction.getUser().getUserId() == userId && transaction.getStatus().equals("Debit")) {
                    BigDecimal transactionAmt = transaction.getTransactionAmt();
                    Wallet wallet = walletRepo.findWalletByUserId(userId);
                    wallet.setAmount(wallet.getAmount().add(transactionAmt));

                    Transaction refundTransaction = new Transaction();
                    refundTransaction.setUser(user.get());
                    refundTransaction.setClosingBalance(wallet.getAmount().add(transactionAmt));
                    refundTransaction.setTransactionAmt(transactionAmt);
                    refundTransaction.setStatus("Refund");
                    refundTransaction.setTransactionDateTime(new Date());
                    String transId = "XX03" + UUID.randomUUID().toString();
                    refundTransaction.setTransactionNo(transId);

                    walletRepo.save(wallet);
                    transactionRepo.save(refundTransaction);

                    return "Your refund of amount " + format.format(transactionAmt) + " has been credited in wallet against the transaction : " + transId +
                            " at " + refundTransaction.getTransactionDateTime() + ".\nYour closing balance is : " +
                            format.format(refundTransaction.getClosingBalance());

                }
                //else display message for invalid refund request
                else {
                    return "Invalid refund request!";
                }
            }
            //else display message for inactive user
            else {
                return "User Inactive. Cannot make transaction!";
            }
        } else {
            return "User doesn't exist!";
        }
    }

    @Override
    public String checkWalletBalance(int userId) {

        BigDecimal walletBalance = walletRepo.findWalletBalanceByUserId(userId);

        //check if wallet balance is not null
        if (walletBalance != null) {
            return "Your wallet balance is : " + format.format(walletBalance);
        }
        //else print no balance
        else {
            return "No balance in the wallet!";
        }
    }

    @Override
    public String payP2P(int fromUserId, BigDecimal amtToBePaid, int toUserId) {


        synchronized (this) {
            Wallet fromUserWallet = walletRepo.findWalletByUserId(fromUserId);
            Optional<User> fromUserOptionalData = userRepo.findById(fromUserId);
            Optional<User> toUserOptionalData = userRepo.findById(toUserId);

            //check if fromUser exists
            if (fromUserOptionalData.isPresent()) {

                //check if fromUser is active
                if (fromUserOptionalData.get().getUserStatus() != 1) {

                    //check if toUser exists
                    if (toUserOptionalData.isPresent()) {

                        //check if toUser is active
                        if (toUserOptionalData.get().getUserStatus() != 1) {

                            //check if sufficient amt is present in fromUser
                            if (fromUserWallet.getAmount().compareTo(amtToBePaid) == 1) {

                                Wallet toUserWallet = walletRepo.findWalletByUserId(toUserId);

                                BigDecimal finalAmt = fromUserWallet.getAmount().subtract(amtToBePaid);
                                fromUserWallet.setAmount(finalAmt);
                                String transId = "XX02" + UUID.randomUUID().toString();

                                Transaction fromTransaction = new Transaction();
                                fromTransaction.setUser(fromUserOptionalData.get());
                                fromTransaction.setTransactionNo(transId);
                                fromTransaction.setTransactionDateTime(new Date());
                                fromTransaction.setStatus("Debit");
                                fromTransaction.setClosingBalance(finalAmt);
                                fromTransaction.setTransactionAmt(amtToBePaid);
                                transactionRepo.save(fromTransaction);
                                walletRepo.save(fromUserWallet);

                                if (toUserWallet != null) {

                                    toUserWallet.setAmount(toUserWallet.getAmount().add(amtToBePaid));
                                    toUserWallet.setUser(toUserOptionalData.get());
                                    walletRepo.save(toUserWallet);
                                } else {
                                    Wallet wallet = new Wallet();
                                    wallet.setAmount(amtToBePaid);
                                    wallet.setUser(toUserOptionalData.get());
                                    walletRepo.save(wallet);
                                }

                                String toTransId = "XX01" + UUID.randomUUID().toString();

                                Transaction toTransaction = new Transaction();
                                toTransaction.setUser(toUserOptionalData.get());
                                toTransaction.setTransactionNo(toTransId);
                                toTransaction.setTransactionDateTime(new Date());
                                toTransaction.setStatus("Credit");
                                toTransaction.setClosingBalance(walletRepo.findWalletBalanceByUserId(toUserId));
                                toTransaction.setTransactionAmt(amtToBePaid);
                                transactionRepo.save(toTransaction);


                                return format.format(amtToBePaid) + " amount has been successfully paid to " + toUserOptionalData.get().getUserName() +
                                        " from your wallet against transaction no : " + transId + " at " + toTransaction.getTransactionDateTime() +
                                        ".\nYour closing balance is : " + format.format(fromTransaction.getClosingBalance());

                            }
                            //display message for in sufficient wallet balance
                            else {
                                return "No sufficient balance in your wallet!";
                            }

                        }
                        //display message for inactive toUser
                        else {
                            return "Cannot pay the amount to " + toUserOptionalData.get().getUserName() + " as the user is Inactive!";
                        }

                    }
                    //display message for invalid toUser
                    else {
                        return "Transaction Failed due to Invalid User Id!";
                    }
                }
                //display message for inactive fromUser
                else {
                    return "Inactive User Id! Cannot make transaction";
                }
            }
            //else display message for invalid fromUser
            else {
                return "Invalid User Id! No user details found!";
            }


        }
    }
}
