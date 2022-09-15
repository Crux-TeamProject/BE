package com.project.crux.domain.response;

import com.project.crux.domain.CrewMember;
import lombok.Getter;

@Getter
public class CrewMemberResponseDto {
    private Long id;
    private String nickname;
    private String content;
    private String status;
    private String imgUrl;


    public CrewMemberResponseDto(CrewMember crewMember) {
        this.id = crewMember.getMember().getId();
        this.nickname = crewMember.getMember().getNickname();
        this.content = crewMember.getMember().getContent();
        this.status = crewMember.getStatus().toString();
        this.imgUrl = crewMember.getMember().getImgUrl();
    }
}
