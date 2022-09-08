package com.project.crux.service;

import com.project.crux.domain.*;
import com.project.crux.domain.response.CrewResponseDto;
import com.project.crux.domain.response.LikeGymResponseDto;
import com.project.crux.domain.response.MypageResponseDto;
import com.project.crux.domain.response.ResponseDto;
import com.project.crux.exception.CustomException;
import com.project.crux.exception.ErrorCode;
import com.project.crux.repository.LikeGymRepository;
import com.project.crux.repository.MemberCrewRepository;
import com.project.crux.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class MypageService {
    //마이 페이지 조회 기능
    private final MemberRepository memberRepository;
    private final LikeGymRepository likeGymRepository;
    private final MemberCrewRepository memberCrewRepository;

    public ResponseDto<?> viewMypage(Long memberId) {
        Member member = memberRepository.findById(memberId).orElse(null);
        if (null == member) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        List<LikeGym> likeGymList = likeGymRepository.findAllByMember(member);
        List<LikeGymResponseDto> likeGymResponseDtos = new ArrayList<>();
        for (LikeGym likeGym : likeGymList) {
            Gym gym = likeGym.getGym();
            likeGymResponseDtos.add(new LikeGymResponseDto(gym));
        }
        List<CrewResponseDto> crewResponseDtos = new ArrayList<>();
        List<MemberCrew> crewList = memberCrewRepository.findAllByMember(member);
        for (MemberCrew memberCrew : crewList) {
            Crew crew = memberCrew.getCrew();
            crewResponseDtos.add(CrewResponseDto.from(crew));
        }
        return ResponseDto.success(new MypageResponseDto(member, crewResponseDtos, likeGymResponseDtos));
    }
}
