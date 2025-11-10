package com.progress.coolProject.DTO.chat;

import lombok.Data;

@Data
public class ChatInputDTO {
    private String message;
    private Long userId;
    private String sessionId;
}
