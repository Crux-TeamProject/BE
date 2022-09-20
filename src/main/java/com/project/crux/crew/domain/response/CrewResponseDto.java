package com.project.crux.crew.domain.response;

import com.project.crux.crew.Status;
import com.project.crux.crew.domain.Crew;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CrewResponseDto {
    private Long id;
    private String name;
    private String content;
    private String imgUrl;
    private int crewNum;
    private int likeNum;
    private boolean like;

    public static CrewResponseDto from(Crew crew) {
        return CrewResponseDto.builder()
                .id(crew.getId())
                .name(crew.getName())
                .content(crew.getContent())
                .imgUrl(crew.getImgUrl())
                .crewNum((int) crew.getCrewMemberList().stream().filter(cm -> !cm.getStatus().equals(Status.SUBMIT)).count())
                .likeNum(crew.getCountOfLike())
                .build();
    }

    public static CrewResponseDto of(Crew crew, boolean like) {
        return CrewResponseDto.builder()
                .id(crew.getId())
                .name(crew.getName())
                .content(crew.getContent())
                .imgUrl(crew.getImgUrl())
                .crewNum((int) crew.getCrewMemberList().stream().filter(cm -> !cm.getStatus().equals(Status.SUBMIT)).count())
                .likeNum(crew.getCountOfLike())
                .like(like)
                .build();
    }
}