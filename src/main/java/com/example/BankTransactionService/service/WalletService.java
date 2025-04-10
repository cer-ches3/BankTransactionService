package com.example.BankTransactionService.service;

import com.example.BankTransactionService.model.entity.Wallet;
import com.example.BankTransactionService.model.request.WalletRequest;
import com.example.BankTransactionService.model.response.ErrorResponse;
import com.example.BankTransactionService.model.response.OkResponse;
import com.example.BankTransactionService.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

@Service
//@Slf4j
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;

    public List<Wallet> getAllWallets() {
        //log.info("Get all wallets.");
        return walletRepository.findAll();
    }

    public ResponseEntity getBalance(UUID walletId) {
        if (!walletRepository.existsById(walletId)) {
            //log.error("Wallet with id {} not found!", walletId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ErrorResponse(MessageFormat.format("Wallet with id {0} not found!", walletId)));
        }

        BigDecimal balance = walletRepository.findById(walletId).get().getBalance();

        //log.info("Get balance for wallet with id {}.", walletId);
        return ResponseEntity.status(HttpStatus.OK).body(balance);
    }

    public ResponseEntity createNewWallet() {
        Wallet newWallet = new Wallet();

        walletRepository.save(newWallet);

        //log.info("Create new wallet.");
        return ResponseEntity.status(HttpStatus.CREATED).body(newWallet);
    }

    @Transactional
    public ResponseEntity performOperation(WalletRequest walletRequest) {
        String operationType = walletRequest.getOperationType().toString();
        double amount = walletRequest.getAmount();
        Wallet existsWallet = walletRepository.findById(walletRequest.getWalletId()).orElse(null);

        if (existsWallet == null) {
            //log.error("Wallet with id {} not found!", walletRequest.getWalletId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ErrorResponse(MessageFormat.format("Wallet with id {0} not found!", walletRequest.getWalletId())));
        }
        if (walletRequest.getAmount() < 0.01) {
            //log.error("Invalid value amount: {}!", walletRequest.getAmount());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    new ErrorResponse(MessageFormat.format("Invalid value amount: {0}!", walletRequest.getAmount())));
        }
        synchronized (existsWallet) {
            switch (operationType) {
                case "DEPOSIT":
                    return deposit(existsWallet, amount);
                case "WITHDRAW":
                    return withdraw(existsWallet, amount);
                default:
                    //log.error("Unknown operation type: {}", operationType);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                            new ErrorResponse(MessageFormat.format("Unknown operation type: {0}!", operationType))
                    );
            }
        }
    }

    private ResponseEntity deposit(Wallet existsWallet, double amount) {
        BigDecimal balance = existsWallet.getBalance();
        balance = balance.add(BigDecimal.valueOf(amount));

        existsWallet.setBalance(balance);
        walletRepository.save(existsWallet);

        //log.info("Perform deposit: {}.", amount);
        return ResponseEntity.status(HttpStatus.OK).body(
                new OkResponse(MessageFormat.format("Perform deposit: walletId {0}, amount {1}.", existsWallet.getWalletId(), amount))
        );
    }

    private ResponseEntity withdraw(Wallet existsWallet, double amount) {
        BigDecimal balance = existsWallet.getBalance();
        BigDecimal amountToWithdraw = BigDecimal.valueOf(amount);

        if (balance.compareTo(BigDecimal.ZERO) == 0 || amountToWithdraw.compareTo(balance) > 0) {
            //log.error("Insufficient funds in the wallet {}.", existsWallet.getWalletId());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    new ErrorResponse(MessageFormat.format("Insufficient funds in the wallet {0}.", existsWallet.getWalletId())));
        }

        balance = balance.subtract(amountToWithdraw);

        existsWallet.setBalance(balance);
        walletRepository.save(existsWallet);

        //log.info("Perform withdraw: {}.", amount);
        return ResponseEntity.status(HttpStatus.OK).body(
                new OkResponse(MessageFormat.format("Perform withdraw: walletId {0}, amount {1}.", existsWallet.getWalletId(), amount))
        );
    }
}
