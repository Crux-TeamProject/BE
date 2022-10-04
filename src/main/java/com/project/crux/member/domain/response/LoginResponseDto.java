package com.project.crux.member.domain.response;

import com.project.crux.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginResponseDto {
    private Long id;
    private String email;
    private String nickname;
    private String imgUrl;

    public LoginResponseDto(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.imgUrl = member.getImgUrl();
    }
}
