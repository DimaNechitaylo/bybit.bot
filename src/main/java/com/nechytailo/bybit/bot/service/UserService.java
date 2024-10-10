package com.nechytailo.bybit.bot.service;

import com.nechytailo.bybit.bot.entity.User;

import java.util.Optional;

public interface UserService {

    Optional<User> getUser(Long userId);

    User registerUser(Long userId, String username);
}
