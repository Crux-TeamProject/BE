package com.project.crux.sse.controller;

import com.project.crux.common.ResponseDto;
import com.project.crux.sse.service.NotificationService;
import com.project.crux.sse.domain.dto.NotificationCountDto;
import com.project.crux.sse.domain.dto.NotificationResponseDto;
import com.project.crux.security.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // api 알림 구독
    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                @RequestParam(required = false, defaultValue = "") String lastEventId) {

        Long memberId = userDetails.getMember().getId();
        System.out.println(lastEventId);
        return notificationService.subscribe(memberId, lastEventId);
    }

    //api 알림 조회
    @GetMapping("/notifications")
    public ResponseDto<List<NotificationResponseDto>> getNotifications(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(notificationService.getNotifications(userDetails));
    }

    //api 알림 읽음 처리
    @PostMapping("/notifications/{notificationId}")
    public ResponseDto<String> readNotification(@PathVariable Long notificationId) {
        return ResponseDto.success(notificationService.readNotification(notificationId));
    }

    //api 현재 읽지않은 알림 갯수
    @GetMapping( "/notifications/count")
    public ResponseDto<NotificationCountDto> countUnReadNotifications(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(notificationService.countUnReadNotifications(userDetails));
    }

    //api 단일 알림 삭제
    @DeleteMapping("/notifications/{notificationId}")
    public ResponseDto<String> deleteNotification(@PathVariable Long notificationId) {
        return ResponseDto.success(notificationService.deleteNotification(notificationId));
    }

    //api 알림 전체 삭제
    @DeleteMapping("/notifications")
    public ResponseDto<String> deleteAllNotification(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(notificationService.deleteAllNotification(userDetails));
    }
}
