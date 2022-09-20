package com.project.crux.crew.domain.response;

import com.project.crux.crew.Status;
import com.project.crux.crew.domain.Crew;
import com.project.crux.crew.domain.Notice;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CrewFindOneResponseDto {
    private Long id;
    private String name;
    private String content;
    private String imgUrl;
    private List<CrewMemberResponseDto> memberList;
    private int crewNum;
    private List<NoticeResponseDto> noticeList;
    private boolean like;

    public CrewFindOneResponseDto(Crew crew, List<Notice> noticeList, boolean like) {
        this.id = crew.getId();
        this.name = crew.getName();
        this.content = crew.getContent();
        this.imgUrl = crew.getImgUrl();
        this.memberList = crew.getCrewMemberList().stream()
                .filter(memberCrew -> !memberCrew.getStatus().equals(Status.SUBMIT))
                .map(CrewMemberResponseDto::new).collect(Collectors.toList());
        this.crewNum = (int) crew.getCrewMemberList().stream().filter(cm -> !cm.getStatus().equals(Status.SUBMIT)).count();
        this.noticeList = noticeList.stream().map(NoticeResponseDto::new).collect(Collectors.toList());
        this.like = like;
    }
}
