package com.example.BankTransactionService.controller;

import com.example.BankTransactionService.model.entity.Wallet;
import com.example.BankTransactionService.model.request.WalletRequest;
import com.example.BankTransactionService.repository.WalletRepository;
import com.example.BankTransactionService.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class WalletControllerTest {

    @Mock
    private WalletService walletService;

    @InjectMocks
    private WalletController walletController;

    private WalletRepository walletRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test get all wallets")
    public void testGetAllWallets() {
        List<Wallet> wallets = new ArrayList<>(List.of(new Wallet()));

        when(walletService.getAllWallets()).thenReturn(wallets);

        List<Wallet> result = walletController.getAllWallets();

        verify(walletService, times(1)).getAllWallets();
    }

    @Test
    @DisplayName("Test get balance")
    public void testGetBalance() {
        UUID walletId = UUID.randomUUID();
        BigDecimal balance = BigDecimal.valueOf(1000.0);

        when(walletService.getBalance(walletId)).thenReturn(ResponseEntity.ok(balance));

        ResponseEntity result = walletController.getBalance(walletId);

        verify(walletService, times(1)).getBalance(walletId);
    }

    @Test
    @DisplayName("Test create new wallet")
    public void testCreateNewWallet() {
        Wallet wallet = new Wallet();

        when(walletService.createNewWallet()).thenReturn(ResponseEntity.ok(wallet));

        ResponseEntity result = walletController.createNewWallet();

        verify(walletService, times(1)).createNewWallet();
    }

    @Test
    @DisplayName("Test perform operation")
    public void testPerformOperation() {
        WalletRequest walletRequest = new WalletRequest();

        when(walletService.performOperation(walletRequest)).thenReturn(ResponseEntity.ok(""));

        ResponseEntity result = walletController.performOperation(walletRequest);

        verify(walletService, times(1)).performOperation(walletRequest);
    }
}
