package com.project.crux.crew.domain.response;

import com.project.crux.crew.domain.Notice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class NoticeResponseDto {
    private Long id;
    private String content;

    public NoticeResponseDto(Notice notice) {
        this.id = notice.getId();
        this.content = notice.getContent();
    }
}
