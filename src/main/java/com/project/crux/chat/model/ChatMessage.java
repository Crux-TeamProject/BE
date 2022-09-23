package com.project.crux.chat.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private MessageType type;
    private String roomId;
    private String sender;
    private String message;
    private List<String> userList;

    public void updateUserList(List<String> users) {
        this.userList = users;
    }

    public enum MessageType {
        ENTER, QUIT, TALK
    }
}
