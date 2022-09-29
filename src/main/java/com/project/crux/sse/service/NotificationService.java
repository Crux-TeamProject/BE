package com.project.crux.sse.service;

import com.project.crux.sse.NotificationType;
import com.project.crux.sse.domain.Notification;
import com.project.crux.sse.domain.NotificationContent;
import com.project.crux.sse.domain.dto.NotificationCountDto;
import com.project.crux.sse.domain.dto.NotificationResponseDto;
import com.project.crux.sse.repository.EmitterRepository;
import com.project.crux.sse.repository.NotificationRepository;
import com.project.crux.member.domain.Member;
import com.project.crux.exception.CustomException;
import com.project.crux.exception.ErrorCode;
import com.project.crux.security.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60; // 1시간

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;

    public SseEmitter subscribe(Long memberId, String lastEventId) {

        String emitterId = makeTimeIncludeId(memberId);

        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        try {
            emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
            emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

            sendToClient(emitter, emitterId, "EventStream Created. [userId=" + memberId + "]");

            if (hasLostData(lastEventId)) {
                sendLostData(lastEventId, memberId, emitter);
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.SUBSCRIBE_FAIL);
        }
        return emitter;
    }

    private String makeTimeIncludeId(Long userId) {
        return userId + "_" + System.currentTimeMillis();
    }

    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }
    private void sendLostData(String lastEventId, Long userId, SseEmitter emitter) {
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithId(String.valueOf(userId));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
        log.info("유실 데이터 전송 완료 ,{}", lastEventId);
    }

    private void sendToClient(SseEmitter emitter, String id, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name("sse")
                    .data(data));
            log.info(" 알림 전송 완료 {}", id);
        } catch (IOException | IllegalStateException exception) {
            emitterRepository.deleteById(id);
            log.info("{}", exception.getMessage());
        }
    }

    @Async("Executor")
    public void send(Member receiver, NotificationType notificationType, NotificationContent content) {
        Notification notification = new Notification(receiver, notificationType, content);
        notificationRepository.save(notification);

        String id = String.valueOf(receiver.getId());

        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllStartWithById(id);
        sseEmitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notification);
                    sendToClient(emitter, key, NotificationResponseDto.from(notification));
                    log.info("알림 전송 내용 : {}",notification.getNotificationContent().getContent());
                    log.info("알림 내용 crewId : {}",notification.getNotificationContent().getCrewId());
                }
        );
    }


    @Transactional
    public List<NotificationResponseDto> getNotifications(UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();
        List<Notification> notifications = notificationRepository.findAllByReceiverOrderByIdDesc(member);
        log.info("{} 님 알림 조회", member.getNickname());

        return notifications.stream()
                .map(NotificationResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public String readNotification(Long notificationId) {
        Notification notification = getNotification(notificationId);
        notification.read();
        log.info("알림 읽음 처리 완료 Id: {}",notificationId);
        return "알림 읽음 처리 완료" + notificationId;
    }

    public NotificationCountDto countUnReadNotifications(UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();
        Long count = notificationRepository.countByIsReadFalseAndReceiver(member);
        return NotificationCountDto.of(count);
    }

    @Transactional
    public String deleteNotification(Long notificationId) {
        Notification notification = getNotification(notificationId);
        notificationRepository.delete(notification);
        log.info("단일 알림 삭제 완료 Id: {}", notificationId);
        return "단일 알림 삭제 완료" + notificationId;
    }

    @Transactional
    public String deleteAllNotification(UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();
        notificationRepository.deleteAllByReceiver(member);
        log.info("{} 님 알림 전체 삭제 완료", member.getNickname());
        return "알림 전체 삭제 완료";
    }

    private Notification getNotification(Long notificationId) {
        return notificationRepository.findById(notificationId)
                .orElseThrow(()-> new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND));
    }

}