package com.example.demo.controllers;

import com.example.demo.entities.LoanEntity;
import com.example.demo.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loans")
@CrossOrigin("*")
public class LoanController {

    @Autowired
    LoanService loanService;

    @PostMapping("/{userId}/make")
    public ResponseEntity<LoanEntity> makeLoan(@RequestBody LoanEntity loan, @PathVariable Long userId) {
        try {
            LoanEntity newLoan = loanService.makeLoan(loan, userId);
            return ResponseEntity.ok(newLoan);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
