package com.subscriptions.subscriptions.repository;

import com.subscriptions.subscriptions.model.SubscribedChannels;
import org.springframework.data.repository.CrudRepository;

public interface SubscribedChannelsRepository extends CrudRepository<SubscribedChannels, Integer> {
}
