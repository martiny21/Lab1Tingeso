package com.example.demo.services;

import com.example.demo.entities.LoanEntity;
import com.example.demo.entities.UserEntity;
import com.example.demo.repositories.LoanRepository;
import com.example.demo.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoanService loanService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testMakeLoanSuccess() {
        UserEntity user = new UserEntity();
        user.setId(1L);

        LoanEntity loan = new LoanEntity();
        loan.setAmount(10000);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(loanRepository.save(any(LoanEntity.class))).thenReturn(loan);

        LoanEntity result = loanService.makeLoan(loan, 1L);

        assertNotNull(result);
        assertEquals(user, result.getUser());
    }

    @Test
    public void testMakeLoanUserNotFound() {
        LoanEntity loan = new LoanEntity();
        loan.setAmount(10000);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            loanService.makeLoan(loan, 1L);
        });

        assertEquals("User not found", exception.getMessage());
    }
}
