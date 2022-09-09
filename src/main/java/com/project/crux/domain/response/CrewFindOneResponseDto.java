package com.project.crux.domain.response;

import com.project.crux.common.Status;
import com.project.crux.domain.Crew;
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

    public CrewFindOneResponseDto(Crew crew) {
        this.id = crew.getId();
        this.name = crew.getName();
        this.content = crew.getContent();
        this.imgUrl = crew.getImgUrl();
        this.memberList = crew.getCrewMemberList().stream()
                .filter(memberCrew -> !memberCrew.getStatus().equals(Status.SUBMIT))
                .map(CrewMemberResponseDto::new).collect(Collectors.toList());
        this.crewNum = crew.getCountOfCrewMemberList();
    }
}
