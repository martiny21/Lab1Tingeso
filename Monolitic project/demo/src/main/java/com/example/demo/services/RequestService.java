package com.example.demo.services;

import com.example.demo.entities.LoanEntity;
import com.example.demo.entities.RequestEntity;
import com.example.demo.entities.UserEntity;
import com.example.demo.repositories.LoanRepository;
import com.example.demo.repositories.RequestRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoanRepository loanRepository;

    public RequestEntity makeRequest(RequestEntity request, Long userId) {
        short status = 1;
        request.setStatus(status);

        // Retrieve the user by their ID from the userRepository
        Optional<UserEntity> user = userRepository.findById(userId);
        if (user.isPresent()) {
            request.setUser(user.get());
        } else {
            throw new RuntimeException("User not found");
        }
        return requestRepository.save(request);
    }

    public RequestEntity evaluateRequest(Long userId) {
        // Retrieve the request by the user ID from the requestRepository
        Optional<RequestEntity> requestOpt = Optional.ofNullable(requestRepository.findByUserId(userId));
        Optional<UserEntity> userOpt = userRepository.findById(userId);

        if(requestOpt.isPresent() && userOpt.isPresent()) {
            RequestEntity request = requestOpt.get();
            Optional<LoanEntity> loanOpt = Optional.ofNullable(loanRepository.findByUserId(userId));

            if(loanOpt.isPresent()) {
                LoanEntity loan = loanOpt.get();
                UserEntity user = userOpt.get();
                boolean result = evaluateCRequest(loan, request, user);
                // Set the status of the request to 3 if the result is false
                if (result) {
                    request.setStatus((short) 3);
                }
                loanRepository.save(loan);

            } else {
                throw new RuntimeException("Loan not found");

            }
            return requestRepository.save(request);

        } else {
            throw new RuntimeException("Request not found");
        }
    }

    public boolean evaluateCRequest(LoanEntity loan, RequestEntity request, UserEntity user) {
        int loanAmount = loan.getAmount();
        float mensualInterestRate = (loan.getInterestRate()/100 ) / 12.0f / 100.0f;
        int totalPayments = loan.getLimitYears() * 12;
        double monthlyPayment = loanAmount * (mensualInterestRate * Math.pow(1 + mensualInterestRate, totalPayments)) / (Math.pow(1 + mensualInterestRate, totalPayments) - 1);

        long income = user.getSalary();
        float paymentIncomeRate = (float) (monthlyPayment / income) * 100.0f;
        if (paymentIncomeRate > 30) {
            request.setStatus((short) 7);
            return false;
        }

        // Check if the user has been in the job for more than 1 year from now
        LocalDate jobStartDate = request.getJobStartDate();
        LocalDate currentDate = LocalDate.now();
        long yearsBetween = ChronoUnit.YEARS.between(jobStartDate, currentDate);
        if (yearsBetween < 1) {
            request.setStatus((short) 7);
            return false;
        }

        //Check debt income rate
        double debtAmount = request.getDebtAmount() + monthlyPayment;
        double debtIncomeRate = (debtAmount / income) * 100.0f;
        if (debtIncomeRate > 50) {
            request.setStatus((short) 7);
            return false;
        }

        //Check user age
        long age = ChronoUnit.YEARS.between(user.getBirthDate(), currentDate);
        long ageEndLoan = age + loan.getLimitYears();
        if (ageEndLoan >= 70) {
            request.setStatus((short) 7);
            return false;
        }

        //Is not checking if the user have saving capacity

        return true;
    }

    public RequestEntity updateRequest(Long requestId, short status) {
        Optional<RequestEntity> requestOpt = requestRepository.findById(requestId);
        if(requestOpt.isPresent()) {
            RequestEntity request = requestOpt.get();
            request.setStatus(status);
            return requestRepository.save(request);
        } else {
            throw new RuntimeException("Request not found");
        }
    }

    public short getRequestStatus(Long requestId) {
        Optional<RequestEntity> requestOpt = requestRepository.findById(requestId);
        if(requestOpt.isPresent()) {
            return requestOpt.get().getStatus();
        } else {
            throw new RuntimeException("Request not found");
        }
    }

    public List<RequestEntity> getAllRequests() {
        return requestRepository.findAll();
    }
}
