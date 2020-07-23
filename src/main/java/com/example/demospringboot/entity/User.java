package com.example.demospringboot.entity;


import com.example.demospringboot.util.Auditable;
import com.example.demospringboot.util.enums.UserStatus;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

/**
 * Entity Class for User
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user")
public class User extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private int userId;
    private String userName;
    private String mobileNo;
    private String emailId;
    private String address1;
    private String address2;

    private UserStatus userStatus = UserStatus.ACTIVE;

    // private int UserStatus userStatus;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "userId")
//    private Wallet wallet;


    @OneToMany(mappedBy = "payer", cascade = CascadeType.ALL)
    // @JoinColumn(name = "userId")
    private List<Transaction> transactionList;

    public User() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    //    public int getUserStatus() {
//        return userStatus;
//    }
//
//    public void setUserStatus(int userStatus) {
//        this.userStatus = userStatus;
//    }


}

