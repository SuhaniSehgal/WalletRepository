package com.example.demospringboot.entity;

import com.example.demospringboot.util.Auditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

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

    private String status;

    private BigDecimal closingBalance;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dateTime")
    private Date transactionDateTime;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    private User user;

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public Date getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(Date transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
