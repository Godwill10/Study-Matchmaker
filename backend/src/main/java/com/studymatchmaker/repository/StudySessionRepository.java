package com.studymatchmaker.repository;

import com.studymatchmaker.model.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudySessionRepository extends JpaRepository<StudySession, Long> {

    List<StudySession> findByCourseIgnoreCase(String course);
}