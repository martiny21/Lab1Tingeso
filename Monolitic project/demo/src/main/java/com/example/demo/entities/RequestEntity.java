package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "Requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestEntity {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (unique = true, nullable = false)
    private long id;

    // status 1 = initial review, 2 = missing documents, 3 = review, 4 = Pre-approved, 5 = final Aprobation, 6 = Aproved,
    // 7 = Rejected, 8 = Canceled, 9 = disbursement
    private short status;
    private LocalDate jobStartDate;
    private double debtAmount;

    //Foreign Keys, this need another look later
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;


}
