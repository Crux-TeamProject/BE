package com.project.crux.domain.response;

import com.project.crux.common.Status;
import com.project.crux.domain.Crew;
import com.project.crux.domain.Notice;
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

    public CrewFindOneResponseDto(Crew crew, List<Notice> noticeList) {
        this.id = crew.getId();
        this.name = crew.getName();
        this.content = crew.getContent();
        this.imgUrl = crew.getImgUrl();
        this.memberList = crew.getCrewMemberList().stream()
                .filter(memberCrew -> !memberCrew.getStatus().equals(Status.SUBMIT))
                .map(CrewMemberResponseDto::new).collect(Collectors.toList());
        this.crewNum = crew.getCountOfCrewMemberList();
        this.noticeList = noticeList.stream().map(NoticeResponseDto::new).collect(Collectors.toList());
    }
}
