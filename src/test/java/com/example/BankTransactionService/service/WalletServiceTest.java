package com.example.BankTransactionService.service;

import com.example.BankTransactionService.model.entity.Wallet;
import com.example.BankTransactionService.model.request.OperationType;
import com.example.BankTransactionService.model.request.WalletRequest;
import com.example.BankTransactionService.model.response.ErrorResponse;
import com.example.BankTransactionService.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletService walletService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test get all wallets")
    public void testGetAllWallets() {
        List<Wallet> wallets = new ArrayList<>();
        wallets.add(new Wallet());

        when(walletRepository.findAll()).thenReturn(wallets);

        List<Wallet> result = walletService.getAllWallets();

        assertEquals(1, wallets.size());
        verify(walletRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test get balance if success")
    public void testGetBalance_ifSuccess() {
        UUID walletId = UUID.randomUUID();
        BigDecimal balance = BigDecimal.valueOf(1000.0);
        Wallet wallet = new Wallet(walletId, balance);

        when(walletRepository.existsById(walletId)).thenReturn(true);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        ResponseEntity result = walletService.getBalance(walletId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(BigDecimal.valueOf(1000.0), result.getBody());
        verify(walletRepository, times(1)).existsById(walletId);
    }

    @Test
    @DisplayName("Test get balance if wallet not found")
    public void testGetBalance_ifWalletNotFound() {
        UUID walletId = UUID.randomUUID();
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                MessageFormat.format("Wallet with id {0} not found!", walletId));
        when(walletRepository.existsById(walletId)).thenReturn(false);

        ResponseEntity result = walletService.getBalance(walletId);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertTrue(result.getBody() instanceof ErrorResponse);
        verify(walletRepository, times(1)).existsById(walletId);
        verify(walletRepository, never()).save(any());
    }

    @Test
    @DisplayName("Test create new wallet")
    public void testCreateNewWallet() {
        ResponseEntity response = walletService.createNewWallet();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @DisplayName("Test perform operation for deposit if wallet not found")
    public void testPerformOperationDeposit_ifWalletNotFound() {
        UUID walletId = UUID.randomUUID();
        WalletRequest walletRequest = new WalletRequest(walletId, OperationType.DEPOSIT, 100);

        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        ResponseEntity result = walletService.performOperation(walletRequest);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertTrue(result.getBody() instanceof ErrorResponse);
        verify(walletRepository, times(1)).findById(walletId);
        verify(walletRepository, never()).save(any());
    }

    @Test
    @DisplayName("Test perform operation for withdraw if wallet not found")
    public void testPerformOperationWithdraw_ifWalletNotFound() {
        UUID walletId = UUID.randomUUID();
        WalletRequest walletRequest = new WalletRequest(walletId, OperationType.WITHDRAW, 100);

        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        ResponseEntity result = walletService.performOperation(walletRequest);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertTrue(result.getBody() instanceof ErrorResponse);
        verify(walletRepository, times(1)).findById(walletId);
        verify(walletRepository, never()).save(any());
    }

    @Test
    @DisplayName("Test perform operation fot deposit if amount is invalid")
    public void testPerformOperationDeposit_ifAmountIsInvalid() {
        UUID walletId = UUID.randomUUID();
        BigDecimal balance = BigDecimal.valueOf(1000);
        Wallet wallet = new Wallet(walletId, balance);
        WalletRequest walletRequest = new WalletRequest(walletId, OperationType.DEPOSIT, 0);

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        ResponseEntity result = walletService.performOperation(walletRequest);

        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertTrue(result.getBody() instanceof ErrorResponse);
        verify(walletRepository, times(1)).findById(walletId);
        verify(walletRepository, never()).save(any());
    }

    @Test
    @DisplayName("Test perform operation fot withdraw if amount is invalid")
    public void testPerformOperationWithdraw_ifAmountIsInvalid() {
        UUID walletId = UUID.randomUUID();
        BigDecimal balance = BigDecimal.valueOf(1000);
        Wallet wallet = new Wallet(walletId, balance);
        WalletRequest walletRequest = new WalletRequest(walletId, OperationType.WITHDRAW, 0);

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        ResponseEntity result = walletService.performOperation(walletRequest);

        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertTrue(result.getBody() instanceof ErrorResponse);
        verify(walletRepository, times(1)).findById(walletId);
        verify(walletRepository, never()).save(any());
    }

    @Test
    @DisplayName("Test perform operation deposit if success")
    public void testPerformOperationDeposit_ifSuccess() {
        UUID walletId = UUID.randomUUID();
        BigDecimal balance = BigDecimal.valueOf(1000.0);
        Wallet wallet = new Wallet(walletId, balance);
        WalletRequest walletRequest = new WalletRequest(walletId, OperationType.DEPOSIT, 100);

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        ResponseEntity result = walletService.performOperation(walletRequest);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(BigDecimal.valueOf(1100.0), wallet.getBalance());
        verify(walletRepository, times(1)).findById(walletId);
        verify(walletRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Test perform operation withdraw if success")
    public void testPerformOperationWithdraw_ifSuccess() {
        UUID walletId = UUID.randomUUID();
        BigDecimal balance = BigDecimal.valueOf(1000.0);
        Wallet wallet = new Wallet(walletId, balance);
        WalletRequest walletRequest = new WalletRequest(walletId, OperationType.WITHDRAW, 100);

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        ResponseEntity result = walletService.performOperation(walletRequest);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(BigDecimal.valueOf(900.0), wallet.getBalance());
        verify(walletRepository, times(1)).findById(walletId);
        verify(walletRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Test perform operation withdraw if insufficient funds")
    public void testPerformOperationWithdraw_ifInsufficientFunds() {
        UUID walletId = UUID.randomUUID();
        BigDecimal balance = BigDecimal.valueOf(1000.0);
        Wallet wallet = new Wallet(walletId, balance);
        WalletRequest walletRequest = new WalletRequest(walletId, OperationType.WITHDRAW, 10000);

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        ResponseEntity result = walletService.performOperation(walletRequest);

        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertEquals(BigDecimal.valueOf(1000.0), wallet.getBalance());
        assertTrue(result.getBody() instanceof ErrorResponse);
        verify(walletRepository, times(1)).findById(walletId);
        verify(walletRepository, never()).save(any());
    }
}
