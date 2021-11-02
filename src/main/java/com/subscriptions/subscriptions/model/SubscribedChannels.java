package com.subscriptions.subscriptions.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;


@Entity
@Data

public class SubscribedChannels {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int subscriptionId;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "channel_id")
    private Channels channel;
    private Date transactionDate;
    private int amount;
    private int noOfTimes;
    private boolean isActive = true;

    public SubscribedChannels(int subscriptionId, User user, Channels channel, Date transactionDate, int amount, int noOfTimes, boolean isActive) {
        this.subscriptionId = subscriptionId;
        this.user = user;
        this.channel = channel;
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.noOfTimes = noOfTimes;
        this.isActive = isActive;
    }


    public SubscribedChannels() {

    }
}





