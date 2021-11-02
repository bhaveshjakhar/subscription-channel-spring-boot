package com.subscriptions.subscriptions.model;


import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Channels {
    @Id
    @GeneratedValue
    private int channelId;
    private String channelName;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "channel", fetch = FetchType.LAZY)
   private List<SubscribedChannels> subscribedChannelsList = new ArrayList<>();

    public String toString(){

        return (channelId + " " + channelName);

    }

}
