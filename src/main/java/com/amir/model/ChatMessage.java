package com.amir.model;

import lombok.Data;
import lombok.ToString;

/**
 * Created by AmirSP on 7/12/2018.
 */
@Data
@ToString
public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;
    private Long identificationNumber;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
}