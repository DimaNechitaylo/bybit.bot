package com.nechytailo.bybit.bot.controller;

import com.nechytailo.bybit.bot.model.Account;
import com.nechytailo.bybit.bot.model.ProxyParams;
import com.nechytailo.bybit.bot.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/accounts") //TODO security
public class AccountController { //TODO delete unused
    @Autowired
    private AccountService accountService;

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    @PostMapping("/add")
    public ResponseEntity<Account> addAccount(@RequestBody Account account) {
        Account createdAccount = accountService.addAccount(account);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

//    @GetMapping("/add2")
//    public ResponseEntity<Account> addAccountTest() {
//        Account account = new Account(null, "S3RbImVJCFZ62uoctc", "xCKm88GVT8Z5Du2FZMcPWYaQJSKWHIF3gpwX",
//                new ProxyParams(null, "testHost0",80,"testLogin0","testPass0" ));
//        Account account1 = new Account(null, "ulaWGp4YMTGccVZQQg", "ZEEFF5d0socspyxzHmH4BUbg8ZjZK5qdjcf8",
//                new ProxyParams(null, "testHost1",80,"testLogin1","testPass1" ));
//        Account createdAccount = accountService.addAccount(account);
//        Account createdAccount1 = accountService.addAccount(account1);
//        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
//    }

}
