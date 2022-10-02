package com.project.crux.crew.domain.response;

import com.project.crux.crew.Status;
import com.project.crux.crew.domain.Crew;
import com.project.crux.crew.domain.Notice;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CrewFindOneResponseDto {
    private Long id;
    private String name;
    private String content;
    private String imgUrl;
    private String mainActivityGym;
    private String mainActivityArea;
    private List<String> keywords;
    private Long hostId;
    private List<CrewMemberResponseDto> memberList;
    private int crewNum;
    private List<CrewNoticeResponseDto> noticeList;
    private boolean like;
    private boolean submit;

    public CrewFindOneResponseDto(Crew crew, List<Notice> noticeList, boolean like, boolean submit) {
        this.id = crew.getId();
        this.name = crew.getName();
        this.content = crew.getContent();
        this.imgUrl = crew.getImgUrl();
        this.mainActivityGym = crew.getMainActivityGym();
        this.mainActivityArea = crew.getMainActivityArea();
        this.keywords = Arrays.asList(crew.getKeywords() == null ? new String[]{} : crew.getKeywords().split(","));
        this.hostId = crew.getCrewMemberList().stream()
                .filter(mc -> mc.getStatus().equals(Status.ADMIN)).findAny().get().getMember().getId();
        this.memberList = crew.getCrewMemberList().stream()
                .filter(cm -> !cm.getStatus().equals(Status.SUBMIT))
                .map(CrewMemberResponseDto::new).collect(Collectors.toList());
        this.crewNum = (int) crew.getCrewMemberList().stream().filter(cm -> !cm.getStatus().equals(Status.SUBMIT)).count();
        this.noticeList = noticeList.stream().map(CrewNoticeResponseDto::new).collect(Collectors.toList());
        this.like = like;
        this.submit = submit;
    }
}
