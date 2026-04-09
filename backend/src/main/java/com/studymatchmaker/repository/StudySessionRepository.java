package com.studymatchmaker.repository;

import com.studymatchmaker.model.StudySession;
import com.studymatchmaker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface StudySessionRepository extends JpaRepository<StudySession, Long> {

    List<StudySession> findByHostOrderByStartTimeDesc(User host);

    @Query("SELECT s FROM StudySession s WHERE :user MEMBER OF s.participants ORDER BY s.startTime DESC")
    List<StudySession> findByParticipant(@Param("user") User user);

    @Query("SELECT s FROM StudySession s WHERE s.status = 'UPCOMING' AND s.startTime > :now ORDER BY s.startTime ASC")
    List<StudySession> findUpcoming(@Param("now") LocalDateTime now);

    @Query("SELECT s FROM StudySession s WHERE s.status = 'UPCOMING' AND s.startTime > :now AND s.host IN :hosts ORDER BY s.startTime ASC")
    List<StudySession> findUpcomingByHosts(@Param("hosts") List<User> hosts, @Param("now") LocalDateTime now);

    @Query("SELECT s FROM StudySession s WHERE (s.host = :user OR :user MEMBER OF s.participants) AND s.startTime > :now ORDER BY s.startTime ASC")
    List<StudySession> findMyUpcoming(@Param("user") User user, @Param("now") LocalDateTime now);
}
