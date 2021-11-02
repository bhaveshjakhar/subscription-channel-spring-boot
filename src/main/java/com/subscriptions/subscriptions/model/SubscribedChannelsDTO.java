package com.subscriptions.subscriptions.model;

import lombok.Data;

import java.util.Date;


@Data

public class SubscribedChannelsDTO {


    private int channel;
    private Date transactionDate;
    private int amount;
    private int noOfTimes;


}
