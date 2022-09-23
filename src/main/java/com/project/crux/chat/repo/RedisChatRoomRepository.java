package com.project.crux.chat.repo;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class RedisChatRoomRepository {
    private static final String CHAT_ROOM_ID_ = "CHAT_ROOM_ID_";
    private static final String SESSION_ID = "SESSION_ID";

    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, String> opsHashEnterRoom;

    @PostConstruct
    private void init() {
        opsHashEnterRoom = redisTemplate.opsForHash();
    }

    //채팅 SubScribe 할 때, WebSocket SessionId 를 통해서 redis에 저장
    public void enterChatRoom(String roomId, String sessionId, String username) {
        opsHashEnterRoom.put(SESSION_ID, sessionId, roomId);
        opsHashEnterRoom.put(CHAT_ROOM_ID_ + roomId, sessionId, username);
    }

    public List<String> getUsers(String roomId) {
        return opsHashEnterRoom.values(CHAT_ROOM_ID_ + roomId).stream().distinct().collect(Collectors.toList());
    }

    // 유저 세션으로 입장해 있는 채팅방 ID 조회
    public String getUserEnterRoomId(String sessionId) {
        return opsHashEnterRoom.get(SESSION_ID, sessionId);
    }

    // 유저 세션정보와 맵핑된 채팅방ID 삭제
    public void removeUserEnterInfo(String sessionId, String roomID) {
        opsHashEnterRoom.delete(SESSION_ID, sessionId);
        opsHashEnterRoom.delete(CHAT_ROOM_ID_ + roomID, sessionId);
    }
}
