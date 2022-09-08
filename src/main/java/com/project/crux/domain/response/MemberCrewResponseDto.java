package com.project.crux.domain.response;

import com.project.crux.domain.MemberCrew;
import lombok.Getter;

@Getter
public class MemberCrewResponseDto {
    private Long id;
    private String nickname;
    private String content;
    private String status;


    public MemberCrewResponseDto(MemberCrew memberCrew) {
        this.id = memberCrew.getMember().getId();
        this.nickname = memberCrew.getMember().getNickname();
        this.content = memberCrew.getMember().getContent();
        this.status = memberCrew.getStatus().toString();
    }
}
