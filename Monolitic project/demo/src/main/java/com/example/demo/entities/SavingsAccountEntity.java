package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "SavingsAccount")
@NoArgsConstructor
@AllArgsConstructor
public class SavingsAccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    private double balance;
    private LocalDate dateCreation;

    @OneToMany(mappedBy = "savingsAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionEntity> transactionsList;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

}
