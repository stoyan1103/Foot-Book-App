package com.example.wallet.service;

import com.example.exception.DomainException;
import com.example.transaction.model.Transaction;
import com.example.transaction.model.TransactionStatus;
import com.example.transaction.model.TransactionType;
import com.example.transaction.service.TransactionService;
import com.example.user.model.User;
import com.example.wallet.model.Wallet;
import com.example.wallet.repository.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return Wallet.builder().owner(user).balance(new BigDecimal("100")).currency(Currency.getInstance("BGN")).createdOn(LocalDateTime.now()).updatedOn(LocalDateTime.now()).build();
    }

    @Override
    @Transactional
    public Transaction depositMoneyToAccount(Long walletId, BigDecimal amount) {
        Wallet wallet = getWalletById(walletId);

        String transactionDescription = "Top up %.2f".formatted(amount.doubleValue());

        applyDeposit(wallet, amount);

        changeUpdatedOn(wallet);

        walletRepository.save(wallet);


        return transactionService.createNewTransaction(
                wallet.getOwner(),
                FOOT_BOOK_LTD,
                walletId.toString(),
                amount,
                wallet.getBalance(),
                wallet.getCurrency(),
                TransactionType.DEPOSIT,
                TransactionStatus.SUCCEEDED,
                transactionDescription,
                null
        );
    }

    private Wallet getWalletById(Long walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new DomainException("Wallet with id [%s] does not exist.".formatted(walletId)));
    }

    private void applyDeposit(Wallet wallet, BigDecimal amount) {
        wallet.setBalance(wallet.getBalance().add(amount));
    }

    private void applyWithdrawal(Wallet wallet, BigDecimal amount) {
        wallet.setBalance(wallet.getBalance().subtract(amount));
    }

    private void changeUpdatedOn(Wallet wallet) {
        wallet.setUpdatedOn(LocalDateTime.now());
    }

    @Override
    @Transactional
    public Transaction charge(User user, Long walletId, BigDecimal amount, String description) {
        Wallet wallet = getWalletById(walletId);

        if (!isUserWalletOwner(user, walletId)) {
            return createFailedTransaction(user, wallet, amount, description, "User does not own this wallet.");
        }

        if (isInsufficientBalance(wallet, amount)) {
            return createFailedTransaction(user, wallet, amount, description, "Insufficient funds.");
        }

        applyWithdrawal(wallet, amount);

        changeUpdatedOn(wallet);

        walletRepository.save(wallet);

        return transactionService.createNewTransaction(
                user,
                wallet.getId().toString(),
                FOOT_BOOK_LTD,
                amount,
                wallet.getBalance(),
                wallet.getCurrency(),
                TransactionType.WITHDRAWAL,
                TransactionStatus.SUCCEEDED,
                description,
                null
        );
    }

    private boolean isUserWalletOwner(User user, Long walletId) {
        return user.getWallet().getId().equals(walletId);
    }

    private boolean isInsufficientBalance(Wallet wallet, BigDecimal amount) {
        return wallet.getBalance().compareTo(amount) < 0;
    }

    private Transaction createFailedTransaction(User user, Wallet wallet, BigDecimal amount,
                                                String description, String failureReason) {

        return transactionService.createNewTransaction(
                user,
                wallet.getId().toString(),
                FOOT_BOOK_LTD,
                amount,
                wallet.getBalance(),
                wallet.getCurrency(),
                TransactionType.WITHDRAWAL,
                TransactionStatus.FAILED,
                description,
                failureReason
        );
    }
}