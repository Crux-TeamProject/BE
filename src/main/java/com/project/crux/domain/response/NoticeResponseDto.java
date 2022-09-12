package com.project.crux.domain.response;

import com.project.crux.domain.Notice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class NoticeResponseDto {
    private String content;

    public NoticeResponseDto(Notice notice) {
        this.content = notice.getContent();
    }
}
