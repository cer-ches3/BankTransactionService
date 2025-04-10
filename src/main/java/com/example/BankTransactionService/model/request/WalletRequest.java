package com.example.BankTransactionService.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WalletRequest {
    private UUID walletId;
    private OperationType operationType;
    private double amount;
}
