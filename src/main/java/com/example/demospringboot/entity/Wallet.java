package com.example.demospringboot.entity;

import com.example.demospringboot.util.Auditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Entity Class for Wallet
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "wallet")
public class Wallet extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int walletId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    private User user;
    private BigDecimal amount;


    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
