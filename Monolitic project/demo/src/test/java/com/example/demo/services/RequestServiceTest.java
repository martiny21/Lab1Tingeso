package com.example.demo.services;

import com.example.demo.entities.LoanEntity;
import com.example.demo.entities.RequestEntity;
import com.example.demo.entities.UserEntity;
import com.example.demo.repositories.LoanRepository;
import com.example.demo.repositories.RequestRepository;
import com.example.demo.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RequestServiceTest {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private RequestService requestService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testMakeRequest() {
        UserEntity user = new UserEntity();
        user.setId(1L);

        RequestEntity request = new RequestEntity();
        request.setJobStartDate(LocalDate.now().minusYears(2));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(requestRepository.save(any(RequestEntity.class))).thenReturn(request);

        RequestEntity result = requestService.makeRequest(request, 1L);

        assertNotNull(result);
        assertEquals(1, result.getStatus());
        assertEquals(user, result.getUser());
    }

    @Test
    public void testEvaluateRequest() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setSalary(5000);
        user.setBirthDate(LocalDate.now().minusYears(30));

        RequestEntity request = new RequestEntity();
        request.setJobStartDate(LocalDate.now().minusYears(2));
        request.setDebtAmount(1000);

        LoanEntity loan = new LoanEntity();
        loan.setAmount(10000);
        loan.setInterestRate((short) 5);
        loan.setLimitYears((short) 5);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(requestRepository.findByUserId(1L)).thenReturn(request);
        when(loanRepository.findByUserId(1L)).thenReturn(loan);
        when(requestRepository.save(any(RequestEntity.class))).thenReturn(request);
        when(loanRepository.save(any(LoanEntity.class))).thenReturn(loan);

        RequestEntity result = requestService.evaluateRequest(1L);

        assertNotNull(result);
        assertEquals(3, result.getStatus());
    }

    @Test
    public void testEvaluateRequestFailsOnFirstIf() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setSalary(5000);

        RequestEntity request = new RequestEntity();
        request.setJobStartDate(LocalDate.now().minusYears(2));
        request.setDebtAmount(1000);

        LoanEntity loan = new LoanEntity();
        loan.setAmount(100000);
        loan.setInterestRate((short) 5);
        loan.setLimitYears((short) 5);

        boolean result = requestService.evaluateCRequest(loan, request, user);

        assertFalse(result);
        assertEquals(7, request.getStatus());
    }

    @Test
    public void testEvaluateRequestFailsOnSecondIf() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setSalary(5000);

        RequestEntity request = new RequestEntity();
        request.setJobStartDate(LocalDate.now().minusMonths(6)); // Less than 1 year

        LoanEntity loan = new LoanEntity();
        loan.setAmount(10000);
        loan.setInterestRate((short) 5);
        loan.setLimitYears((short) 5);

        boolean result = requestService.evaluateCRequest(loan, request, user);

        assertFalse(result);
        assertEquals(7, request.getStatus());
    }

    @Test
    public void testEvaluateRequestFailsOnThirdIf() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setSalary(5000);

        RequestEntity request = new RequestEntity();
        request.setJobStartDate(LocalDate.now().minusYears(2));
        request.setDebtAmount(10000);

        LoanEntity loan = new LoanEntity();
        loan.setAmount(10000);
        loan.setInterestRate((short) 5);
        loan.setLimitYears((short) 5);

        // Set the user's birth date to 18 years ago
        user.setBirthDate(LocalDate.now().minusYears(18));

        boolean result = requestService.evaluateCRequest(loan, request, user);

        assertFalse(result);
        assertEquals(7, request.getStatus());
    }

    @Test
    public void testEvaluateRequestFailsOnfourthIf() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setSalary(5000);

        RequestEntity request = new RequestEntity();
        request.setJobStartDate(LocalDate.now().minusYears(2));
        request.setDebtAmount(1000);

        LoanEntity loan = new LoanEntity();
        loan.setAmount(10000);
        loan.setInterestRate((short) 5);
        loan.setLimitYears((short) 5);

        // Set the user's birth date to 70 years ago
        user.setBirthDate(LocalDate.now().minusYears(70));

        boolean result = requestService.evaluateCRequest(loan, request, user);

        assertFalse(result);
        assertEquals(7, request.getStatus());
    }

    @Test
    public void testUpdateRequest() {
        RequestEntity request = new RequestEntity();
        request.setId(1L);
        request.setStatus((short) 1);

        when(requestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(requestRepository.save(any(RequestEntity.class))).thenReturn(request);

        RequestEntity result = requestService.updateRequest(1L, (short) 2);

        assertNotNull(result);
        assertEquals(2, result.getStatus());
    }

    @Test
    public void testGetRequestStatus() {
        RequestEntity request = new RequestEntity();
        request.setId(1L);
        request.setStatus((short) 1);

        when(requestRepository.findById(1L)).thenReturn(Optional.of(request));

        short status = requestService.getRequestStatus(1L);

        assertEquals(1, status);
    }

    @Test
    public void testGetAllRequests() {
        List<RequestEntity> requests = List.of(new RequestEntity(), new RequestEntity());

        when(requestRepository.findAll()).thenReturn(requests);

        List<RequestEntity> result = requestService.getAllRequests();

        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
