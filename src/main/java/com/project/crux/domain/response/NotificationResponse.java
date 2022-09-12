package com.project.crux.domain.response;

import com.project.crux.sse.Notification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private Long id;
    private String content;
    private Boolean status;

    public static NotificationResponse from(Notification notification) {

        return new NotificationResponse(notification.getId(),notification.getContent(),
                notification.getIsRead());
    }
}