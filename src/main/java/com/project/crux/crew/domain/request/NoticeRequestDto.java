package com.project.crux.crew.domain.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NoticeRequestDto {
    private String date;
    private String place;
    private String content;
}
