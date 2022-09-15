package com.project.crux.domain.response;

import com.project.crux.domain.Crew;
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
                .crewNum(crew.getCountOfCrewMemberList())
                .likeNum(crew.getCountOfLike())
                .build();
    }
}
