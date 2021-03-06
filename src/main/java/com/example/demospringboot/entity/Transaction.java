package com.example.demospringboot.entity;

import com.example.demospringboot.util.Auditable;
import com.example.demospringboot.util.enums.TransactionDirection;
import com.example.demospringboot.util.enums.TransactionStatus;
import com.example.demospringboot.util.enums.TransactionType;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Entity for Transaction
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "transaction")
public class Transaction extends Auditable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int transactionId;

    private String transactionNo;

    private BigDecimal transactionAmt;

    private BigDecimal closingBalance;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payer")
    private User payer;

    private int payeeId;

    private TransactionType transactionType;

    private TransactionDirection transactionDirection;

    private TransactionStatus transactionStatus;

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public User getPayer() {
        return payer;
    }

    public void setPayer(User payer) {
        this.payer = payer;
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

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
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

    public int getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(int payeeId) {
        this.payeeId = payeeId;
    }
}
