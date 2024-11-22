package com.example.demo.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserDao {
    @Autowired
    private UserRepository repository;

    public void save(User user) {
        if (user.getId() == null || user.getId().isEmpty()) {
            user.setId(UUID.randomUUID().toString());
        }
        repository.save(user);
    }

    public User findById(String id) {
        return repository.findById(id).orElse(null);
    }

    public boolean existsById(String id) {
        return repository.existsById(id);
    }
}
