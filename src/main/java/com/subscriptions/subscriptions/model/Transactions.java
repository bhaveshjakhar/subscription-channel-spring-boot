package com.subscriptions.subscriptions.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data

public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transactionId;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    @JsonBackReference
    private User user;
    private int subscriptionId;
    private Date transactionDate;
    private int amount;
    private int openingBal;
    private int closingBal;

    public Transactions(int transactionId, User user, int subscriptionId, Date transactionDate, int amount, int openingBal, int closingBal) {
        this.transactionId = transactionId;
        this.user = user;
        this.subscriptionId = subscriptionId;
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.openingBal = openingBal;
        this.closingBal = closingBal;
    }

    public Transactions() {


    }
public Transactions (User user, int subscriptionId, int amount, int openingBal, int closingBal)
{
    this.transactionDate = new Date();
    this.user = user;
    this.subscriptionId = subscriptionId;
    this.amount = amount;
    this.openingBal = openingBal;
    this.closingBal = closingBal;
}
}

