package com.nechytailo.bybit.bot.repository;

import com.nechytailo.bybit.bot.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
