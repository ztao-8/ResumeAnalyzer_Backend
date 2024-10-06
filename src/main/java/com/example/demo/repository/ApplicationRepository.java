package com.example.demo.repository;

import com.example.demo.model.Application;
import com.example.demo.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    // JpaRepository already provides built-in methods for save, findById, etc.
    List<Application> findByUserId(Long userId);

    Optional<Application> findByUserIdAndJobId(Long userId, Long jobId);
}