package com.example.demospringboot.model;

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
    private String status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(BigDecimal closingBalance) {
        this.closingBalance = closingBalance;
    }
}
