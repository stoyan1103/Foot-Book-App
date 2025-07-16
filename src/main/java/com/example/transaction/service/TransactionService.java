package com.example.transaction.service;


import com.example.transaction.model.Transaction;
import com.example.transaction.model.TransactionStatus;
import com.example.transaction.model.TransactionType;
import com.example.user.model.User;

import java.math.BigDecimal;
import java.util.Currency;

public interface TransactionService {

    Transaction createNewTransaction(User walletOwner, String sender, String receiver,
                                     BigDecimal transactionAmount, BigDecimal balanceLeft,
                                     Currency currency, TransactionType type,
                                     TransactionStatus status, String transactionDescription, String failureReason);
}