package com.project.crux.chat.repo;

import com.project.crux.chat.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class RedisChatRoomRepository {
    private static final String CHAT_MESSAGE = "CHAT_MESSAGE";
    private static final String CHAT_ROOM_ID_ = "CHAT_ROOM_ID_";
    private static final String SESSION_ID = "SESSION_ID";

    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, String> opsHashEnterRoom;
    private HashOperations<String, String, List<ChatMessage>> opsHashChatMessage;

    @PostConstruct
    private void init() {
        opsHashEnterRoom = redisTemplate.opsForHash();
        opsHashChatMessage = redisTemplate.opsForHash();
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

    //redis 에 메세지 저장하기
    public ChatMessage save(ChatMessage chatMessage) {
        //chatMessage 를 redis 에 저장하기 위하여 직렬화 한다.
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessage.class));
        String roomId = chatMessage.getRoomId();
        //redis에 저장되어있는 리스트를 가져와, 새로 받아온 chatmessageDto를 더하여 다시 저장한다.
        List<ChatMessage> chatMessageList = opsHashChatMessage.get(CHAT_MESSAGE, roomId);
        //가져온 List가 null일때 새로운 리스트를 만든다 == 처음에 메세지를 저장할경우 리스트가 없기때문에.
        if (chatMessageList == null) {
            chatMessageList = new ArrayList<>();
        }
        chatMessageList.add(chatMessage);
        opsHashChatMessage.put(CHAT_MESSAGE, roomId, chatMessageList);
        redisTemplate.expire(CHAT_MESSAGE,24, TimeUnit.HOURS);
        return chatMessage;
    }

    //채팅 리스트 가져오기
    public List<ChatMessage> findAllMessage(String roomId) {
        return (opsHashChatMessage.get(CHAT_MESSAGE, roomId));
    }
}
