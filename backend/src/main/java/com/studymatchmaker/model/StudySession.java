package com.studymatchmaker.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudySession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String course;

    private String topic;

    private LocalDateTime dateTime;

    private String location;

    private int maxParticipants;

    private int currentParticipants = 0;

    private String level;
    private String mode;
}