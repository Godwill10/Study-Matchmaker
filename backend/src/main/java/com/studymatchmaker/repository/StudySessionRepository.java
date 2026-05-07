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

    @Query("""
            SELECT DISTINCT s
            FROM StudySession s
            JOIN s.participants p
            WHERE p.id = :userId
            ORDER BY s.startTime DESC
            """)
    List<StudySession> findByParticipantId(@Param("userId") Long userId);

    @Query("""
            SELECT s
            FROM StudySession s
            WHERE s.status = com.studymatchmaker.model.StudySession.Status.UPCOMING
              AND s.startTime > :now
            ORDER BY s.startTime ASC
            """)
    List<StudySession> findUpcoming(@Param("now") LocalDateTime now);

    @Query("""
            SELECT s
            FROM StudySession s
            WHERE s.status = com.studymatchmaker.model.StudySession.Status.UPCOMING
              AND s.startTime > :now
              AND s.host.id IN :hostIds
            ORDER BY s.startTime ASC
            """)
    List<StudySession> findUpcomingByHostIds(
            @Param("hostIds") List<Long> hostIds,
            @Param("now") LocalDateTime now
    );

    @Query("""
            SELECT DISTINCT s
            FROM StudySession s
            LEFT JOIN s.participants p
            WHERE s.startTime > :now
              AND (s.host.id = :userId OR p.id = :userId)
            ORDER BY s.startTime ASC
            """)
    List<StudySession> findMyUpcomingByUserId(
            @Param("userId") Long userId,
            @Param("now") LocalDateTime now
    );
}
