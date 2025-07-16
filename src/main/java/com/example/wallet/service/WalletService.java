package com.example.wallet.service;


import com.example.transaction.model.Transaction;
import com.example.user.model.User;
import com.example.wallet.model.Wallet;

import java.math.BigDecimal;

public interface WalletService {

    Wallet initalizeWallet(User user);

    Transaction depositMoneyToAccount(Long walletId, BigDecimal amount);

    Transaction charge(User user, Long walletId, BigDecimal amount, String description);
}