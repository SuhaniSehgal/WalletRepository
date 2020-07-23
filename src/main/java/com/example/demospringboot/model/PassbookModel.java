package com.example.demospringboot.model;

import com.example.demospringboot.util.enums.TransactionDirection;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Model Class for Passbook Details to be returned to the user
 */
public class PassbookModel {

    private int transactionId;
    private String transactionNo;
    private Date transactionDateTime;
    private BigDecimal transactionAmt;
    private TransactionDirection transactionDirection;
    private BigDecimal closingBalance;

    public PassbookModel() {

    }


    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public Date getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(Date transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public BigDecimal getTransactionAmt() {
        return transactionAmt;
    }

    public void setTransactionAmt(BigDecimal transactionAmt) {
        this.transactionAmt = transactionAmt;
    }

    public TransactionDirection getStatus() {
        return transactionDirection;
    }

    public void setStatus(TransactionDirection status) {
        this.transactionDirection = status;
    }

    public BigDecimal getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(BigDecimal closingBalance) {
        this.closingBalance = closingBalance;
    }
}
