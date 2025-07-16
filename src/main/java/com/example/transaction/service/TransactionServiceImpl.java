package com.example.transaction.service;

import com.example.transaction.model.Transaction;
import com.example.transaction.model.TransactionStatus;
import com.example.transaction.model.TransactionType;
import com.example.transaction.repository.TransactionRepository;
import com.example.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction createNewTransaction(User walletOwner, String sender, String receiver, BigDecimal transactionAmount,
                                            BigDecimal balanceLeft, Currency currency, TransactionType type,
                                            TransactionStatus status, String transactionDescription, String failureReason) {

        Transaction transaction = Transaction.builder()
                .owner(walletOwner)
                .sender(sender)
                .receiver(receiver)
                .amount(transactionAmount)
                .balanceLeft(balanceLeft)
                .currency(currency)
                .type(type)
                .status(status)
                .description(transactionDescription)
                .failureReason(failureReason)
                .createdOn(LocalDateTime.now())
                .build();

        return transactionRepository.save(transaction);
    }
}