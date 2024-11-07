package com.example.demo.repositories;

import com.example.demo.entities.SavingsAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingsAccountRepository extends JpaRepository<SavingsAccountEntity, Long> {
    SavingsAccountEntity findByUserId(Long userId);

    @Override
    <S extends SavingsAccountEntity> S save(S entity);
}
