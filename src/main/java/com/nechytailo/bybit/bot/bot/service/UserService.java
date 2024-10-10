package com.nechytailo.bybit.bot.bot.service;

import com.nechytailo.bybit.bot.bot.model.User;

import java.util.Optional;

public interface UserService {

    Optional<User> getUser(Long userId);

    User saveUser(User user);

    User registerUser(User user);

}
