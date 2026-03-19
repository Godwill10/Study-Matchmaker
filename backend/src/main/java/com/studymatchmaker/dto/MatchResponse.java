package com.studymatchmaker.dto;

public class MatchResponse {

    private Long id;
    private String topic;
    private String level;
    private String mode;
    private int score;

    public MatchResponse(Long id, String topic, String level, String mode, int score) {
        this.id = id;
        this.topic = topic;
        this.level = level;
        this.mode = mode;
        this.score = score;
    }

    public Long getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }

    public String getLevel() {
        return level;
    }

    public String getMode() {
        return mode;
    }

    public int getScore() {
        return score;
    }
}