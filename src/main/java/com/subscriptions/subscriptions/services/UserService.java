package com.subscriptions.subscriptions.services;

import com.subscriptions.subscriptions.model.*;
import com.subscriptions.subscriptions.repository.ChannelsRepository;
import com.subscriptions.subscriptions.repository.SubscribedChannelsRepository;
import com.subscriptions.subscriptions.repository.TransactionsRepository;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.subscriptions.subscriptions.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TransactionsRepository transactionsRepository;

    @Autowired
    private SubscribedChannelsRepository subscribedChannelsRepository;

    @Autowired
    EntityManager em;

    @Autowired
    private ChannelsRepository channelsRepository;

    public List<User> getAllUsers() {

        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public User getUser(int id) {
        return userRepository.findById(id).get();
    }


    public void addMoney(String username, int amount) {

        User user = userRepository.findByEmail(username).get();
        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);

    }

    public void addUser(User user) {


        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }


    public void addUsers(List<User> users) {
        users.stream().forEach(user -> user.setPassword(passwordEncoder.encode(user.getPassword())));
        users.stream().forEach(user -> userRepository.save(user));
    }


//    public void addNewSubscriptionChannel(String username , SubscribedChannels subscribedChannel)
//
//    {
//        User user = userRepository.findByEmail(username).get();
//        subscribedChannel.setUser(user);
//        subscribedChannelsRepository.save(subscribedChannel);
//
//    }


    public void addNewSubscriptionChannel(String username, SubscribedChannelsDTO subscribedChannelsDTO) {
        User user = userRepository.findByEmail(username).get();
        SubscribedChannels subscribedChannel = new SubscribedChannels();
        subscribedChannel.setUser(user);
        subscribedChannel.setAmount(subscribedChannelsDTO.getAmount());
        subscribedChannel.setTransactionDate(subscribedChannelsDTO.getTransactionDate());
        subscribedChannel.setNoOfTimes(subscribedChannelsDTO.getNoOfTimes());
        Channels channel = channelsRepository.findById(subscribedChannelsDTO.getChannel()).get();
        subscribedChannel.setChannel(channel);

        subscribedChannelsRepository.save(subscribedChannel);

    }

    public void deductMonthlySubscription(String username) {

        User user = userRepository.findByEmail(username).get();
        Query query = em.createNativeQuery("select * FROM Subscribed_channels where id = ? AND transaction_date <= ? AND is_active = 1", SubscribedChannels.class);

        query.setParameter(1, user);
        query.setParameter(2, new Date());
        List<SubscribedChannels> subscribedChannelsList = query.getResultList();
        if (subscribedChannelsList.size() != 0) {

            subscribedChannelsList.stream().forEach((subscribedChannel) -> {

                int openingBal = user.getBalance();
                if (openingBal >= subscribedChannel.getAmount()) {


                    subscribedChannel.setNoOfTimes(subscribedChannel.getNoOfTimes() - 1);
                    if (subscribedChannel.getNoOfTimes() == 0) {
                        subscribedChannel.setActive(false);
                    }
                    user.setBalance(user.getBalance() - subscribedChannel.getAmount());
                    int closingBal = user.getBalance();

                    Transactions transactions = new Transactions(user, subscribedChannel.getSubscriptionId(), subscribedChannel.getAmount(), openingBal, closingBal);

                    userRepository.save(user);
                    transactionsRepository.save(transactions);
                    subscribedChannelsRepository.save(subscribedChannel);
                }
            });
        }
    }

    public String removeSubscriptionChannel(String username, int channel_id) {
        User  user = userRepository.findByEmail(username).get();
        Channels channel = channelsRepository.findById(channel_id).get();
        Query query = em.createNativeQuery("select * from subscribed_channels where id=? AND channel_id=?", SubscribedChannels.class);
        query.setParameter(1, user);
        query.setParameter(2, channel);
        List<SubscribedChannels> subscribedChannelsList = query.getResultList();
        if(subscribedChannelsList.size()==0){
            return "Invalid Channel ID";
        }
        else{
            SubscribedChannels subscribedChannel = subscribedChannelsList.get(0);
            subscribedChannel.setActive(false);
            subscribedChannelsRepository.save(subscribedChannel);
            return "Channel Unsubscribed";
        }
    }

    public List<Transactions> getStatement(String username, String from, String to){
        User user = userRepository.findByEmail(username).get();
        Query query = em.createNativeQuery("select * from transactions where id=? AND transaction_date BETWEEN ? AND ?", Transactions.class);
        query.setParameter(1, user);
        query.setParameter(2, from);
        query.setParameter(3, to);
        List<Transactions> transactionsList = (List<Transactions>) query.getResultList();
        return transactionsList;
    }


//        User user = userRepository.findByEmail(username).get();
//        int openingBal = user.getBalance();
//        user.setBalance(user.getBalance() - 20);
//        int closingBal = user.getBalance();
//         Transactions transaction = new Transactions(user, 0, 20, openingBal, closingBal);
//        transactionsRepository.save(transaction);
//
//        userRepository.save(user);


//    public  void  deductSubscriptionChannel(String username, int channelId) {
//
//        User user = userRepository.findByEmail(username).get();
//
//        Query query = em.createNativeQuery("select * from subscribed_channels where id = ? AND channel_id = ? ORDER BY subscription_id desc LIMIT 1", SubscribedChannels.class);
//
//        List<SubscribedChannels> list = query.getResultList();
//
//        SubscribedChannels subscribedChannel = list.get(0);
//
//        if (list.size() == 0 && !subscribedChannel.isActive())
//
//
//            public void removeSubscriptionChannel ( int id){
//
//            User user = userRepository.findById(id).get();


    //}
}
