package com.example.demospringboot.service.serviceImpl;

import com.example.demospringboot.entity.Transaction;
import com.example.demospringboot.entity.User;
import com.example.demospringboot.entity.Wallet;
import com.example.demospringboot.model.ResponseModel;
import com.example.demospringboot.repo.TransactionRepo;
import com.example.demospringboot.repo.UserRepo;
import com.example.demospringboot.repo.WalletRepo;
import com.example.demospringboot.service.WalletService;
import com.example.demospringboot.util.enums.*;
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

    private int count = 0;

    //format for currency to be displayed to the user
    private NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
    @Autowired
    private WalletRepo walletRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private TransactionRepo transactionRepo;

    @Override
    public ResponseModel topup(int userId, BigDecimal amtToBeAdded) {
        String transId = "XX" + count++ + UUID.randomUUID().toString();
        ResponseModel responseModel = new ResponseModel();
        responseModel.setTransactionAmt(amtToBeAdded);
        responseModel.setTransactionNo(transId);
        responseModel.setTransactionDate(new Date());

        Optional<Wallet> walletOptionalData = Optional.ofNullable(walletRepo.findWalletByUserId(userId));
        Optional<User> user = userRepo.findById(userId);

        if (user.isPresent()) {

            initTransaction(user, walletOptionalData, transId, amtToBeAdded, TransactionType.TOPUP, TransactionDirection.CREDIT);

            //check whether the user is in active state
            if (user.get().getUserStatus() != UserStatus.INACTIVE) {

                //check if wallet details already exist for a user, then add the amt to the existing wallet amt
                if (walletOptionalData.isPresent()) {
                    Wallet wallet = walletOptionalData.get();

                    //check if wallet status is inactive
                    if (wallet.getWalletStatus() != WalletStatus.INACTIVE) {
                        BigDecimal finalAmt = wallet.getAmount().add(amtToBeAdded);
                        wallet.setAmount(finalAmt);

                        Transaction transaction = transactionRepo.findTransactionByTxnNo(transId);
                        transaction.setClosingBalance(finalAmt);
                        transaction.setTransactionStatus(TransactionStatus.SUCCESS);
                        transactionRepo.save(transaction);
                        walletRepo.save(wallet);

                        responseModel.setClosingBalance(finalAmt);
                        responseModel.setTransactionDate(transaction.getCreatedDate());
                        responseModel.setTransactionDirection(TransactionDirection.CREDIT);
                        responseModel.setTransactionStatus(TransactionStatus.SUCCESS);

                        return responseModel;
                    }
                    //else return the details to user
                    else {
                        Transaction transaction = transactionRepo.findTransactionByTxnNo(transId);
                        transaction.setTransactionStatus(TransactionStatus.FAILURE);
                        transactionRepo.save(transaction);
                        responseModel.setTransactionStatus(TransactionStatus.FAILURE);
                        responseModel.setErrorCode(ErrorCodes.WALLET_INACTIVE);
                        return responseModel;
                    }


                }

                //else create a new wallet with the amt requested for topup
                else {
                    Wallet wallet = new Wallet();
                    wallet.setAmount(amtToBeAdded);

                    //check if the user exists
                    if (user.isPresent()) {
                        wallet.setUser(user.get());

                        Transaction transaction = transactionRepo.findTransactionByTxnNo(transId);
                        transaction.setClosingBalance(amtToBeAdded);
                        transaction.setTransactionStatus(TransactionStatus.SUCCESS);
                        transactionRepo.save(transaction);
                        walletRepo.save(wallet);

                        responseModel.setClosingBalance(amtToBeAdded);
                        responseModel.setTransactionDate(transaction.getCreatedDate());
                        responseModel.setTransactionDirection(TransactionDirection.CREDIT);
                        responseModel.setTransactionStatus(TransactionStatus.SUCCESS);

                        return responseModel;

                    }
                    //if user doesn't exist, display message
                    else {
                        responseModel.setTransactionStatus(TransactionStatus.FAILURE);
                        responseModel.setErrorCode(ErrorCodes.USER_INVALID);
                        return responseModel;
                    }

                }
            }
            //if user inactive, display message to the user
            else {
                Transaction transaction = transactionRepo.findTransactionByTxnNo(transId);
                transaction.setTransactionStatus(TransactionStatus.FAILURE);
                transactionRepo.save(transaction);
                if (walletOptionalData.isPresent()) {
                    responseModel.setClosingBalance(walletOptionalData.get().getAmount());
                }
                responseModel.setTransactionStatus(TransactionStatus.FAILURE);
                responseModel.setErrorCode(ErrorCodes.USER_INACTIVE);
                return responseModel;
            }

        } else {
            responseModel.setTransactionStatus(TransactionStatus.FAILURE);
            responseModel.setErrorCode(ErrorCodes.USER_INVALID);
            return responseModel;
        }

    }

    private void initTransaction(Optional<User> user, Optional<Wallet> walletOptionalData, String transId, BigDecimal amt, TransactionType type, TransactionDirection direction) {
        Transaction transaction = new Transaction();
        transaction.setTransactionNo(transId);
        transaction.setTransactionAmt(amt);
        transaction.setTransactionDirection(direction);
        transaction.setTransactionType(type);
        if (user.isPresent()) {
            transaction.setPayer(user.get());
        }
        if (walletOptionalData.isPresent()) {
            transaction.setClosingBalance(walletOptionalData.get().getAmount());
        }

        if (type != TransactionType.REFUND)
            transactionRepo.save(transaction);

    }

    @Override
    public ResponseModel pay(int userId, BigDecimal amtToBePaid) {

        String transId = "XX" + count++ + UUID.randomUUID().toString();
        ResponseModel responseModel = new ResponseModel();
        responseModel.setTransactionAmt(amtToBePaid);
        responseModel.setTransactionNo(transId);
        responseModel.setTransactionDate(new Date());


        Optional<Wallet> walletOptionalData = Optional.ofNullable(walletRepo.findWalletByUserId(userId));
        Optional<User> user = userRepo.findById(userId);

        if (user.isPresent()) {

            initTransaction(user, walletOptionalData, transId, amtToBePaid, TransactionType.PAY, TransactionDirection.DEBIT);

            //check if user is active
            if (user.get().getUserStatus() != UserStatus.INACTIVE) {

                //check if wallet details for the user exists
                if (walletOptionalData.isPresent()) {
                    Wallet wallet = walletOptionalData.get();

                    if (wallet.getWalletStatus() != WalletStatus.INACTIVE) {
                        BigDecimal walletBalance = wallet.getAmount();

                        //check if the user has sufficient balance in the wallet to pay certain amt
                        if (walletBalance.compareTo(amtToBePaid) == 1) {
                            BigDecimal finalAmt = walletBalance.subtract(amtToBePaid);
                            wallet.setAmount(finalAmt);


                            Transaction transaction = transactionRepo.findTransactionByTxnNo(transId);
                            transaction.setClosingBalance(finalAmt);
                            transaction.setTransactionStatus(TransactionStatus.SUCCESS);
                            transactionRepo.save(transaction);
                            walletRepo.save(wallet);

                            responseModel.setClosingBalance(finalAmt);
                            responseModel.setTransactionDate(transaction.getCreatedDate());
                            responseModel.setTransactionDirection(TransactionDirection.DEBIT);
                            responseModel.setTransactionStatus(TransactionStatus.SUCCESS);

                            return responseModel;

                        }

                        //else display message for insufficient amt
                        else {
                            Transaction transaction = transactionRepo.findTransactionByTxnNo(transId);
                            transaction.setTransactionStatus(TransactionStatus.FAILURE);
                            transactionRepo.save(transaction);
                            responseModel.setTransactionStatus(TransactionStatus.FAILURE);
                            responseModel.setErrorCode(ErrorCodes.INSUFFICIENT_BALANCE);
                            return responseModel;
                        }
                    } else {
                        Transaction transaction = transactionRepo.findTransactionByTxnNo(transId);
                        transaction.setTransactionStatus(TransactionStatus.FAILURE);
                        transactionRepo.save(transaction);
                        responseModel.setTransactionStatus(TransactionStatus.FAILURE);
                        responseModel.setErrorCode(ErrorCodes.WALLET_INACTIVE);
                        return responseModel;
                    }
                }
                //else display message for no wallet amount
                else {
                    Transaction transaction = transactionRepo.findTransactionByTxnNo(transId);
                    transaction.setTransactionStatus(TransactionStatus.FAILURE);
                    transactionRepo.save(transaction);
                    responseModel.setTransactionStatus(TransactionStatus.FAILURE);
                    responseModel.setErrorCode(ErrorCodes.INSUFFICIENT_BALANCE);
                    return responseModel;
                }
            }
            //if user is inactive, display message
            else {
                Transaction transaction = transactionRepo.findTransactionByTxnNo(transId);
                transaction.setTransactionStatus(TransactionStatus.FAILURE);
                transactionRepo.save(transaction);
                responseModel.setTransactionStatus(TransactionStatus.FAILURE);
                responseModel.setErrorCode(ErrorCodes.USER_INACTIVE);
                return responseModel;
            }
        } else {

            responseModel.setTransactionStatus(TransactionStatus.FAILURE);
            responseModel.setErrorCode(ErrorCodes.USER_INVALID);
            return responseModel;
        }

    }

    @Override
    public ResponseModel refund(int userId, int transactionId) {

        Transaction transaction = transactionRepo.getTransactionDetailsForTransactionId(transactionId);
        Optional<User> user = userRepo.findById(userId);
        Optional<Wallet> optionalWallet = Optional.ofNullable(walletRepo.findWalletByUserId(userId));

        String transId = "XX" + count++ + UUID.randomUUID().toString();
        ResponseModel responseModel = new ResponseModel();
        //responseModel.setTransactionAmt(transaction.getTransactionAmt());
        responseModel.setTransactionNo(transId);
        responseModel.setTransactionDate(new Date());

        if (user.isPresent()) {

            initTransaction(user, optionalWallet, transId, transaction.getTransactionAmt(), TransactionType.REFUND, TransactionDirection.CREDIT);

            //check is the user is active
            if (user.get().getUserStatus() != UserStatus.INACTIVE) {

                Wallet wallet = optionalWallet.get();

                //Validate the user id and status as debit
                if (transaction.getPayer().getUserId() == userId && transaction.getTransactionType() == TransactionType.PAY) {
                    BigDecimal transactionAmt = transaction.getTransactionAmt();
                    wallet.setAmount(wallet.getAmount().add(transactionAmt));

                    walletRepo.save(wallet);

                    transaction.setTransactionAmt(transactionAmt);
                    transaction.setClosingBalance(wallet.getAmount());
                    transaction.setTransactionType(TransactionType.REFUND);
                    transaction.setTransactionStatus(TransactionStatus.SUCCESS);

                    transactionRepo.save(transaction);

                    responseModel.setTransactionDirection(TransactionDirection.CREDIT);
                    responseModel.setTransactionStatus(TransactionStatus.SUCCESS);
                    responseModel.setTransactionAmt(transactionAmt);
                    responseModel.setClosingBalance(wallet.getAmount());

                    return responseModel;


                }

                //else display message for invalid refund request
                else {

                    transaction.setTransactionStatus(TransactionStatus.FAILURE);
                    transactionRepo.save(transaction);
                    responseModel.setTransactionStatus(TransactionStatus.FAILURE);
                    responseModel.setErrorCode(ErrorCodes.INVALID_REFUND_REQUEST);
                    return responseModel;
                }
            }
            //else display message for inactive user
            else {
                transaction.setTransactionStatus(TransactionStatus.FAILURE);
                transactionRepo.save(transaction);
                responseModel.setTransactionStatus(TransactionStatus.FAILURE);
                responseModel.setErrorCode(ErrorCodes.USER_INACTIVE);
                return responseModel;
            }
        } else {
            responseModel.setTransactionStatus(TransactionStatus.FAILURE);
            responseModel.setErrorCode(ErrorCodes.USER_INVALID);
            return responseModel;
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
    public ResponseModel payP2P(int fromUserId, BigDecimal amtToBePaid, int toUserId) {

        String transId = "XX" + count++ + UUID.randomUUID().toString();
        ResponseModel responseModel = new ResponseModel();
        responseModel.setTransactionAmt(amtToBePaid);
        responseModel.setTransactionNo(transId);
        responseModel.setTransactionDate(new Date());
        responseModel.setPayeeId(toUserId);

        synchronized (this) {
            Optional<Wallet> optionalFromUserWallet = Optional.ofNullable(walletRepo.findWalletByUserId(fromUserId));
            Optional<User> fromUserOptionalData = userRepo.findById(fromUserId);
            Optional<User> toUserOptionalData = userRepo.findById(toUserId);
            responseModel.setPayeeName(toUserOptionalData.get().getUserName());


            Wallet fromUserWallet = optionalFromUserWallet.get();
            responseModel.setClosingBalance(fromUserWallet.getAmount());
            //check if fromUser exists
            if (fromUserOptionalData.isPresent()) {

                initTransaction(fromUserOptionalData, optionalFromUserWallet, transId, amtToBePaid, TransactionType.P2P, TransactionDirection.DEBIT);

                //check if fromUser is active
                if (fromUserOptionalData.get().getUserStatus() != UserStatus.INACTIVE) {

                    //check if toUser exists
                    if (toUserOptionalData.isPresent()) {

                        //check if toUser is active
                        if (toUserOptionalData.get().getUserStatus() != UserStatus.INACTIVE) {

                            responseModel.setPayeeName(toUserOptionalData.get().getUserName());

                            String toTransId = "XX" + count++ + UUID.randomUUID().toString();

                            Optional<Wallet> optionalToUserWallet = Optional.ofNullable(walletRepo.findWalletByUserId(toUserId));

                            initTransaction(fromUserOptionalData, optionalToUserWallet, toTransId, amtToBePaid, TransactionType.P2P, TransactionDirection.CREDIT);

                            //check if sufficient amt is present in fromUser
                            if (fromUserWallet.getAmount().compareTo(amtToBePaid) == 1) {

                                Wallet toUserWallet = optionalToUserWallet.get();
                                BigDecimal finalAmt = fromUserWallet.getAmount().subtract(amtToBePaid);
                                fromUserWallet.setAmount(finalAmt);


                                Transaction fromTransaction = transactionRepo.findTransactionByTxnNo(transId);
                                fromTransaction.setClosingBalance(finalAmt);
                                fromTransaction.setTransactionStatus(TransactionStatus.SUCCESS);
                                fromTransaction.setPayeeId(toUserId);
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


                                Transaction toTransaction = transactionRepo.findTransactionByTxnNo(toTransId);
                                toTransaction.setPayeeId(toUserId);
                                toTransaction.setTransactionStatus(TransactionStatus.SUCCESS);
                                toTransaction.setClosingBalance(walletRepo.findWalletBalanceByUserId(toUserId));
                                transactionRepo.save(toTransaction);

                                responseModel.setTransactionStatus(TransactionStatus.SUCCESS);
                                responseModel.setClosingBalance(finalAmt);
                                responseModel.setTransactionDirection(TransactionDirection.DEBIT);
                                responseModel.setPayeeId(toUserId);

                                return responseModel;

                            }
                            //display message for in sufficient wallet balance
                            else {

                                Transaction transaction = transactionRepo.findTransactionByTxnNo(transId);
                                transaction.setTransactionStatus(TransactionStatus.FAILURE);
                                transactionRepo.save(transaction);
                                responseModel.setTransactionStatus(TransactionStatus.FAILURE);
                                responseModel.setErrorCode(ErrorCodes.USER_INACTIVE);
                                return responseModel;
                            }

                        }
                        //display message for inactive toUser
                        else {
                            Transaction transaction = transactionRepo.findTransactionByTxnNo(transId);
                            transaction.setTransactionStatus(TransactionStatus.FAILURE);
                            transactionRepo.save(transaction);
                            responseModel.setTransactionStatus(TransactionStatus.FAILURE);
                            responseModel.setErrorCode(ErrorCodes.PAYEE_INACTIVE);
                            return responseModel;
                        }

                    }
                    //display message for invalid toUser
                    else {
                        responseModel.setTransactionStatus(TransactionStatus.FAILURE);
                        responseModel.setErrorCode(ErrorCodes.PAYEE_INVALID);
                        return responseModel;
                    }
                }
                //display message for inactive fromUser
                else {
                    Transaction transaction = transactionRepo.findTransactionByTxnNo(transId);
                    transaction.setTransactionStatus(TransactionStatus.FAILURE);
                    transactionRepo.save(transaction);
                    responseModel.setTransactionStatus(TransactionStatus.FAILURE);
                    responseModel.setErrorCode(ErrorCodes.USER_INACTIVE);
                    return responseModel;
                }
            }
            //else display message for invalid fromUser
            else {
                responseModel.setTransactionStatus(TransactionStatus.FAILURE);
                responseModel.setErrorCode(ErrorCodes.USER_INVALID);
                return responseModel;
            }


        }
    }
}
