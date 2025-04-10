package com.example.BankTransactionService.controller;

import com.example.BankTransactionService.model.entity.Wallet;
import com.example.BankTransactionService.model.request.WalletRequest;
import com.example.BankTransactionService.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    @GetMapping
    public List<Wallet> getAllWallets() {
        return walletService.getAllWallets();
    }

    @GetMapping("/{walletUuid}")
    public ResponseEntity getBalance(@PathVariable UUID walletUuid) {
        return walletService.getBalance(walletUuid);
    }

    @PostMapping("/create")
    public ResponseEntity createNewWallet() {
        return walletService.createNewWallet();
    }

    @PostMapping
    public ResponseEntity performOperation(@RequestBody WalletRequest walletRequest) {
        return walletService.performOperation(walletRequest);
    }
}
