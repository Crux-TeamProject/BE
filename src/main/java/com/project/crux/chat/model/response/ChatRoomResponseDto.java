package com.project.crux.chat.model.response;

import com.project.crux.chat.model.ChatRoom;
import lombok.Getter;

@Getter
public class ChatRoomResponseDto {
    private final Long roomId;
    private final String crewName;
    private final String imgUrl;

    public ChatRoomResponseDto(ChatRoom chatRoom) {
        this.roomId = chatRoom.getId();
        this.crewName = chatRoom.getCrew().getName();
        this.imgUrl = chatRoom.getCrew().getImgUrl();
    }
}
