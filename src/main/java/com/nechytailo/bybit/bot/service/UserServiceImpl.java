package com.nechytailo.bybit.bot.service;

import com.nechytailo.bybit.bot.entity.User;
import com.nechytailo.bybit.bot.entity.UserRole;
import com.nechytailo.bybit.bot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> getUser(Long userId) {
        return userRepository.getUserById(userId);
    }

    @Override
    public User registerUser(Long userId, String username) {
        return userRepository.save(
                User.builder()
                        .id(userId)
                        .username(username)
                        .userRole(UserRole.DEFAULT)
                        .build()
        );
    }
}
