/*
package com.example.BankTransactionService;

import com.example.BankTransactionService.model.entity.Wallet;
import com.example.BankTransactionService.model.request.OperationType;
import com.example.BankTransactionService.model.request.WalletRequest;
import com.example.BankTransactionService.repository.WalletRepository;
import com.example.BankTransactionService.service.WalletService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BankTransactionServiceApplicationTests {

    @LocalServerPort
    private Integer port;

    public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"));

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletService walletService;

    @BeforeAll
    public static void beforeAll() {
        container.start();
    }

    @AfterAll
    public static void afterAll() {
        container.stop();
    }

    @DynamicPropertySource
    public static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

    @Test
    @DisplayName("test get connection with database")
    public void testConnectionWithDB() {
        UUID walletId = UUID.randomUUID();
        BigDecimal balance = BigDecimal.valueOf(1000.0);
        Wallet wallet = new Wallet(walletId, balance);
        WalletRequest walletRequestForDeposit = new WalletRequest(walletId, OperationType.DEPOSIT, 300);
        WalletRequest walletRequestForWithdraw = new WalletRequest(walletId, OperationType.WITHDRAW, 456.7);

        when(walletRepository.existsById(walletId)).thenReturn(true);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        walletService.performOperation(walletRequestForDeposit);
        walletService.performOperation(walletRequestForWithdraw);

        assertEquals(BigDecimal.valueOf(843.3), wallet.getBalance());
        verify(walletRepository, times(2)).save(any());
    }
}
*/
