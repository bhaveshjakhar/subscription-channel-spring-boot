package com.subscriptions.subscriptions.repository;

import com.subscriptions.subscriptions.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
        Optional<User> findByEmail(String username);
}
