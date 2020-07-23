package com.example.demospringboot.model;

import com.example.demospringboot.util.enums.ErrorCodes;
import com.example.demospringboot.util.enums.TransactionDirection;
import com.example.demospringboot.util.enums.TransactionStatus;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;

public class ResponseModel {

    private String transactionNo;
    private BigDecimal transactionAmt;
    private BigDecimal closingBalance;
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate;
    private int payeeId;
    private String payeeName;
    private TransactionDirection transactionDirection;
    private TransactionStatus transactionStatus;
    private ErrorCodes errorCode;

    public ResponseModel() {
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public BigDecimal getTransactionAmt() {
        return transactionAmt;
    }

    public void setTransactionAmt(BigDecimal transactionAmt) {
        this.transactionAmt = transactionAmt;
    }

    public BigDecimal getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(BigDecimal closingBalance) {
        this.closingBalance = closingBalance;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public int getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(int payeeId) {
        this.payeeId = payeeId;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public TransactionDirection getTransactionDirection() {
        return transactionDirection;
    }

    public void setTransactionDirection(TransactionDirection transactionDirection) {
        this.transactionDirection = transactionDirection;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public ErrorCodes getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCodes errorCode) {
        this.errorCode = errorCode;
    }
}
