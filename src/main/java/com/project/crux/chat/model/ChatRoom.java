package com.project.crux.chat.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ChatRoom implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;

    private String roomId;
    private String crewName;

    public static ChatRoom create(String crewId) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = crewId;
        return chatRoom;
    }

    public static ChatRoom create(String crewId, String crewName) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = crewId;
        chatRoom.crewName = crewName;
        return chatRoom;
    }
}
