package com.example.wallet.service;

import com.example.exception.DomainException;
import com.example.transaction.model.Transaction;
import com.example.transaction.service.TransactionService;
import com.example.user.model.User;
import com.example.wallet.model.Wallet;
import com.example.wallet.repository.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Optional;

@Slf4j
@Service
public class WalletServiceImpl implements WalletService {

    private static final String FOOT_BOOK_LTD = "Foot Book Ltd";

    private final WalletRepository walletRepository;
    private final TransactionService transactionService;

    @Autowired
    public WalletServiceImpl(WalletRepository walletRepository, TransactionService transactionService) {
        this.walletRepository = walletRepository;
        this.transactionService = transactionService;
    }

    @Override
    public Wallet initalizeWallet(User user) {
        Optional<Wallet> userWallet = walletRepository.findByOwnerUsername(user.getUsername());

        if (userWallet.isPresent()) {
            throw new DomainException("User with id [%s] already has wallet.".formatted(user.getId()));
        }

        Wallet wallet = walletRepository.save(createWallet(user));

        log.info("Successfully create new wallet with id [%s] and balance [%.2f].".formatted(wallet.getId(), wallet.getBalance()));

        return wallet;
    }

    private Wallet createWallet(User user) {

        return Wallet.builder()
                .owner(user)
                .balance(new BigDecimal("100"))
                .currency(Currency.getInstance("BGN"))
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
    }

    @Override
    public Transaction depositMoneyToAccount(Long walletId, BigDecimal amount) {
        // TODO
        // I am up to here
        return null;
    }
}