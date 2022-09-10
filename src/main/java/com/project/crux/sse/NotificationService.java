package com.project.crux.sse;

import com.project.crux.domain.Member;
import com.project.crux.domain.response.NotificationResponse;
import com.project.crux.exception.CustomException;
import com.project.crux.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60; // 1시간

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;

    public SseEmitter subscribe(Long userId, String lastEventId) {

        // emitter 마다 고유값 발급
        String emitterId = makeTimeIncludeId(userId);

        // 생성된 emitter 저장
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        try {
            // emitter 시간 만료후 삭제
            emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
            emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

            // 503 에러를 방지하기 위한 더미 이벤트 전송
            sendToClient(emitter, emitterId, "EventStream Created. [userId=" + userId + "]");

            // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
            if (hasLostData(lastEventId)) {
                sendLostData(lastEventId, userId, emitter);
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.SUBSCRIBE_FAIL);
        }
        return emitter;
    }

    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    // 받지못한 데이터가 있다면 last - event - id를 기준으로 그 뒤의 데이터를 추출해 알림을 보내주면 된다.
    private void sendLostData(String lastEventId, Long userId, SseEmitter emitter) {
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithId(String.valueOf(userId));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
    }

    private void sendToClient(SseEmitter emitter, String id, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(id);
            throw new RuntimeException("연결 오류!");
        }
    }

    @Async
    public void send(Member receiver, NotificationType notificationType, String content) {
        Notification notification = createNotification(receiver, notificationType, content);
        notificationRepository.save(notification);

        String id = String.valueOf(receiver.getId());

        // 로그인 한 유저의 SseEmitter 모두 가져오기
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllStartWithById(id);
        sseEmitters.forEach(
                (key, emitter) -> {
                    // 데이터 캐시 저장(유실된 데이터 처리하기 위함)
                    emitterRepository.saveEventCache(key, notification);
                    // 데이터 전송
                    sendToClient(emitter, key, NotificationResponse.from(notification));
                }
        );
    }

    private Notification createNotification(Member receiver, NotificationType notificationType, String content) {
        return Notification.builder()
                .receiver(receiver)
                .content(content)
                .notificationType(notificationType)
                .isRead(false)
                .build();
    }

    private String makeTimeIncludeId(Long userId) {
        return userId + "_" + System.currentTimeMillis();
    }

}