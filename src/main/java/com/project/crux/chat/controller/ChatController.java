package com.project.crux.chat.controller;

import com.project.crux.chat.model.ChatMessage;
import com.project.crux.chat.service.ChatService;
import com.project.crux.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private final TokenProvider jwtTokenProvider;
    private final ChatService chatService;

    /**
     * websocket "/pub/chat/message"로 들어오는 메시지를 처리한다.
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessage message, @Header(AUTHORIZATION_HEADER) String bearerToken) {
        String token = jwtTokenProvider.extractToken(bearerToken);
        String nickname = jwtTokenProvider.getNicknameFrom(token);
        // 로그인 회원 정보로 대화명 설정
        message.setSender(nickname);
        message.setType(ChatMessage.MessageType.TALK);
        // Websocket에 발행된 메시지를 redis로 발행(publish)
        chatService.sendChatMessage(message);
    }
}
