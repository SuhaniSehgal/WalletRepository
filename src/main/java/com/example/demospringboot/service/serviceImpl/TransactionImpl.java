package com.example.demospringboot.service.serviceImpl;

import com.example.demospringboot.entity.Transaction;
import com.example.demospringboot.exceptions.ResourceNotFoundException;
import com.example.demospringboot.model.PassbookModel;
import com.example.demospringboot.repo.TransactionRepo;
import com.example.demospringboot.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implementation for Transaction Service
 */
@Service
@Transactional
public class TransactionImpl implements TransactionService {

    @Autowired
    private TransactionRepo transactionRepo;


    @Override
    public List<PassbookModel> getPassbookTransactionsForUser(int id) {
        List<Transaction> allTransactions = transactionRepo.getAllTransactionsForUserPassbook(id);

        //check if transactions exist for the corresponding
        if (allTransactions.size() > 0) {

            Iterator itr = allTransactions.iterator();
            List<PassbookModel> passbookList = new ArrayList<>();
            while (itr.hasNext()) {
                PassbookModel passbookModel = new PassbookModel();
                Transaction transaction = (Transaction) itr.next();
                passbookModel.setTransactionId(transaction.getTransactionId());
                passbookModel.setTransactionNo(transaction.getTransactionNo());
                passbookModel.setTransactionDateTime(transaction.getTransactionDateTime());
                passbookModel.setTransactionAmt(transaction.getTransactionAmt());
                passbookModel.setStatus(transaction.getStatus());
                passbookModel.setClosingBalance(transaction.getClosingBalance());
                passbookList.add(passbookModel);

            }
            return passbookList;
        } else {
            throw new ResourceNotFoundException("No passbook details for the userId : " + id);
        }
    }
}
