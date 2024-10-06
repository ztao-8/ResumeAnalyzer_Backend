package com.example.demo.repository;

import com.example.demo.model.ResumeSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResumeSuggestionRepository extends JpaRepository<ResumeSuggestion, Long> {
    Optional<ResumeSuggestion> findByResumeId(Long resumeId);
}
