package com.project.crux.member.domain.response;

import com.project.crux.crew.domain.response.CrewResponseDto;
import com.project.crux.gym.domain.response.LikeGymResponseDto;
import com.project.crux.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MypageResponseDto {
    private Long id;
    private String nickname;
    private String content;
    private List<CrewResponseDto> crewList;
    private List<LikeGymResponseDto> gymList;

    private List<CrewResponseDto> likeCrewList;
    private String imgUrl;

    public MypageResponseDto(Member member, List<CrewResponseDto> crewResponseDtos, List<LikeGymResponseDto> likeGymResponseDtos, List<CrewResponseDto> likeCrewList) {
        this.id = member.getId();
        this.nickname =member.getNickname();
        this.content = member.getContent();
        this.crewList = crewResponseDtos;
        this.gymList = likeGymResponseDtos;
        this.imgUrl = member.getImgUrl();
        this.likeCrewList = likeCrewList;
    }
}
