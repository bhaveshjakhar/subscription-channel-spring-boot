package com.subscriptions.subscriptions.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jdk.jfr.DataAmount;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @Column(unique = true)
    private String email;
    private int balance = 0;
    private String password;
    private String roles = "ROLE_USER";
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<SubscribedChannels> subscribedChannelsList = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Transactions> transactionsList = new ArrayList<>();


    public User(int id, String name, String email, int balance, String password, String roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.balance = balance;
        this.password = password;
        this.roles = roles;
    }

    public User(String email) {
        this.email = email;
    }

    public User() {

    }
}
