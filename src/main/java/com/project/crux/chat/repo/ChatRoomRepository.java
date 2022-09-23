package com.project.crux.chat.repo;

import com.project.crux.chat.model.ChatRoom;
import com.project.crux.crew.domain.Crew;
import com.project.crux.crew.repository.CrewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatRoomRepository {
    private static final String CHAT_ROOMS = "CHAT_ROOM";
    private static final String CHAT_ROOM_ID_ = "CHAT_ROOM_ID_";
    private static final String SESSION_ID = "SESSION_ID";

    private final CrewRepository crewRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, ChatRoom> hashOpsChatRoom;
    private HashOperations<String, String, String> opsHashEnterRoom;

    @PostConstruct
    private void init() {
        hashOpsChatRoom = redisTemplate.opsForHash();
        opsHashEnterRoom = redisTemplate.opsForHash();

        List<Crew> crews = crewRepository.findAll();
        for (Crew crew : crews) {
            hashOpsChatRoom.put(CHAT_ROOMS,
                    crew.getId().toString(),
                    ChatRoom.create(crew.getId().toString(), crew.getName())
            );
        }
    }

    // 모든 채팅방 조회
    public List<ChatRoom> findAllRoom() {
        return hashOpsChatRoom.values(CHAT_ROOMS);
    }

    // 특정 채팅방 조회
    public ChatRoom findRoomById(String id) {
        return hashOpsChatRoom.get(CHAT_ROOMS, id);
    }

    // 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다.
    public ChatRoom createChatRoom(String crewId) {
        ChatRoom chatRoom = ChatRoom.create(crewId);
        hashOpsChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }

    //채팅 SubScribe 할 때, WebSocket SessionId 를 통해서 redis에 저장
    public void enterChatRoom(String roomId, String sessionId, String username) {
        opsHashEnterRoom.put(SESSION_ID, sessionId, roomId);
        opsHashEnterRoom.put(CHAT_ROOM_ID_ + roomId, sessionId, username);
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
