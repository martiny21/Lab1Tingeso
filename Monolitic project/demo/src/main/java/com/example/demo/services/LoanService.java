package com.example.demo.services;

import com.example.demo.entities.LoanEntity;
import com.example.demo.entities.UserEntity;
import com.example.demo.repositories.LoanRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private UserRepository userRepository;

    public LoanEntity makeLoan(LoanEntity loan, Long userId) {

        Optional<UserEntity> user = userRepository.findById(userId);
        if (user.isPresent()) {
            loan.setUser(user.get());
            return loanRepository.save(loan);
        } else {
            throw new RuntimeException("User not found");
        }

    }
}
