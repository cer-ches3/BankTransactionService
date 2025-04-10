package com.example.BankTransactionService.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "wallets")
@Getter
@Setter
@AllArgsConstructor
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "wallet_id")
    private UUID walletId;

    @Column(name = "wallet_balance", nullable = false)
    private BigDecimal balance;

    public Wallet() {
        this.balance = BigDecimal.ZERO;
    }
}
