package com.project.crux.member.service;

import com.project.crux.crew.Status;
import com.project.crux.crew.domain.Crew;
import com.project.crux.crew.domain.CrewMember;
import com.project.crux.crew.domain.LikeCrew;
import com.project.crux.crew.repository.LikeCrewRepository;
import com.project.crux.crew.repository.CrewMemberRepository;
import com.project.crux.gym.domain.Gym;
import com.project.crux.gym.domain.LikeGym;
import com.project.crux.member.domain.Member;
import com.project.crux.member.domain.request.MypageRequestDto;
import com.project.crux.crew.domain.response.CrewResponseDto;
import com.project.crux.gym.domain.response.LikeGymResponseDto;
import com.project.crux.member.domain.response.MypageResponseDto;
import com.project.crux.common.ResponseDto;
import com.project.crux.exception.CustomException;
import com.project.crux.exception.ErrorCode;
import com.project.crux.gym.repository.LikeGymRepository;
import com.project.crux.member.repository.MemberRepository;
import com.project.crux.security.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class MypageService {
    //마이 페이지 조회 기능
    private final MemberRepository memberRepository;
    private final LikeGymRepository likeGymRepository;
    private final CrewMemberRepository crewMemberRepository;

    private final LikeCrewRepository likeCrewRepository;

    public ResponseDto<?> viewMypage(Long memberId) {
        Member member = memberRepository.findById(memberId).orElse(null);
        if (null == member) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        List<CrewResponseDto> likeCrewList = new ArrayList<>();
        List<LikeCrew> likes = likeCrewRepository.findAllByMember(member);
        for (LikeCrew like : likes) {
            Crew crew = like.getCrew();
            likeCrewList.add(CrewResponseDto.from(crew));
        }

        List<LikeGym> likeGymList = likeGymRepository.findAllByMember(member);
        List<LikeGymResponseDto> likeGymResponseDtos = new ArrayList<>();
        for (LikeGym likeGym : likeGymList) {
            Gym gym = likeGym.getGym();
            likeGymResponseDtos.add(new LikeGymResponseDto(gym));
        }
        List<CrewResponseDto> crewResponseDtos = new ArrayList<>();
        List<CrewMember> crewList = crewMemberRepository.findAllByMember(member);
        for (CrewMember crewMember : crewList) {
            if (!crewMember.getStatus().equals(Status.SUBMIT)) {
                Crew crew = crewMember.getCrew();
                crewResponseDtos.add(CrewResponseDto.from(crew));
            }
        }
        return ResponseDto.success(new MypageResponseDto(member, crewResponseDtos, likeGymResponseDtos, likeCrewList));
    }
    @Transactional
    public ResponseDto<?> editMypage(UserDetailsImpl userDetails, MypageRequestDto mypageRequestDto) {
        Member member = memberRepository.findById(userDetails.getMember().getId()).orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
        member.update(mypageRequestDto);
       return ResponseDto.success("수정 완료");
    }
}
