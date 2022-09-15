package com.project.crux.domain.response;

import com.project.crux.domain.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MypageResponseDto {
    private String nickname;
    private String content;
    private List<CrewResponseDto> crewList;
    private List<LikeGymResponseDto> gymList;
    private String imgUrl;

    public MypageResponseDto(Member member,List<CrewResponseDto> crewResponseDtos,List<LikeGymResponseDto> likeGymResponseDtos) {
        this.nickname =member.getNickname();
        this.content = member.getContent();
        this.crewList = crewResponseDtos;
        this.gymList = likeGymResponseDtos;
        this.imgUrl = member.getImgUrl();
    }
}
