package com.project.crux.domain.response;

import com.project.crux.common.Status;
import com.project.crux.domain.Crew;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class FindOneCrewResponseDto {
    private Long id;
    private String name;
    private String content;
    private String imgUrl;
    private List<MemberCrewResponseDto> memberList;
    private int crewNum;

    public FindOneCrewResponseDto(Crew crew) {
        this.id = crew.getId();
        this.name = crew.getName();
        this.content = crew.getContent();
        this.imgUrl = crew.getImgUrl();
        this.memberList = crew.getMemberCrewList().stream()
                .filter(memberCrew -> !memberCrew.getStatus().equals(Status.SUBMIT))
                .map(MemberCrewResponseDto::new).collect(Collectors.toList());
        this.crewNum = crew.getCountOfMemberCrewList();
    }
}
