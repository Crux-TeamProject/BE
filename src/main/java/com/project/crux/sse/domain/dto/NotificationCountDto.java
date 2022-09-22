package com.project.crux.sse.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationCountDto {
    private Long count;

    public static NotificationCountDto of(Long count) {
        return NotificationCountDto.builder()
                .count(count)
                .build();
    }
}
