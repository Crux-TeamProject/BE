package com.project.crux.crew.domain.response;

import com.project.crux.crew.domain.Notice;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CrewNoticeResponseDto {
    private Long noticeId;
    private String date;
    private String place;
    private String content;
    private Long authorId;
    private String authorNickname;
    private String authorProfileImg;
    private String authorStatus;
    private LocalDateTime createdAt;

    public CrewNoticeResponseDto(Notice notice) {
        this.noticeId = notice.getId();
        this.date = notice.getDate();
        this.place = notice.getPlace();
        this.content = notice.getContent();
        this.authorId = notice.getCrewMember().getMember().getId();
        this.authorNickname = notice.getCrewMember().getMember().getNickname();
        this.authorProfileImg = notice.getCrewMember().getMember().getImgUrl();
        this.authorStatus = notice.getCrewMember().getStatus().toString();
        this.createdAt = notice.getCreatedAt();
    }
}
