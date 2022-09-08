package com.project.crux.service;

import com.project.crux.common.Status;
import com.project.crux.domain.Crew;
import com.project.crux.domain.Member;
import com.project.crux.domain.MemberCrew;
import com.project.crux.domain.request.CrewRequestDto;
import com.project.crux.domain.response.CrewResponseDto;
import com.project.crux.domain.response.CrewFindOneResponseDto;
import com.project.crux.exception.CustomException;
import com.project.crux.exception.ErrorCode;
import com.project.crux.repository.CrewRepository;
import com.project.crux.repository.MemberCrewRepository;
import com.project.crux.repository.MemberRepository;
import com.project.crux.security.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CrewService {

    private static final String DEFAULT_IMAGE_URL = "";

    private final CrewRepository crewRepository;
    private final MemberCrewRepository memberCrewRepository;
    private final MemberRepository memberRepository;

    public CrewResponseDto createCrew(CrewRequestDto crewRequestDto, UserDetailsImpl userDetails) {
        Member member = getMember(userDetails.getMember());
        String imgUrl = getImgUrl(crewRequestDto);
        Crew savedCrew = crewRepository.save(new Crew(crewRequestDto.getName(), crewRequestDto.getContent(), imgUrl));

        MemberCrew memberCrew = new MemberCrew(member, savedCrew);
        memberCrew.updateStatus(Status.ADMIN);
        memberCrewRepository.save(memberCrew);

        return CrewResponseDto.from(savedCrew);
    }

    private String getImgUrl(CrewRequestDto crewRequestDto) {
        return crewRequestDto.getImgUrl() == null ? DEFAULT_IMAGE_URL : crewRequestDto.getImgUrl();
    }

    @Transactional(readOnly = true)
    public List<CrewResponseDto> findAllCrew(Long lastCrewId, int size) {
        verifyLastCrewId(lastCrewId);
        PageRequest pageRequest = PageRequest.of(0, size);
        return crewRepository.findByIdLessThanOrderByIdDesc(lastCrewId, pageRequest)
                .stream().map(CrewResponseDto::from).collect(Collectors.toList());
    }

    private void verifyLastCrewId(Long lastCrewId) {
        if (lastCrewId < 0) {
            throw new CustomException(ErrorCode.INVALID_ARTICLEID);
        }
    }

    @Transactional(readOnly = true)
    public Page<CrewResponseDto> findAllPopularCrew(Pageable pageable) {
        return crewRepository.findAll(pageable).map(CrewResponseDto::from);
    }

    public String registerSubmit(Long crewId, UserDetailsImpl userDetails) {

        Crew crew = getCrew(crewId);
        Member member = userDetails.getMember();

        if(memberCrewRepository.findByCrewAndMember(crew,member).isPresent()){
            throw new CustomException(ErrorCode.ADMIN_REGISTER_SUBMIT);
        }

        MemberCrew memberCrew = new MemberCrew(member, crew);
        memberCrewRepository.save(memberCrew);
        return "크루 가입 신청 완료";
    }

    public String registerPermit(Long crewId, Long memberId) {

        Crew crew = getCrew(crewId);
        Member member = memberRepository.findById(memberId).orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        MemberCrew memberCrew = getMemberCrew(crew, member);
        memberCrew.updateStatus(Status.PERMIT);
        return "크루 가입 승인 완료";
    }

    @Transactional(readOnly = true)
    public CrewFindOneResponseDto findCrew(Long crewId) {
        Crew crew = getCrew(crewId);
        return new CrewFindOneResponseDto(crew);
    }

    public CrewResponseDto updateCrew(Long crewId, CrewRequestDto crewRequestDto, UserDetailsImpl userDetails) {
        Crew crew = getCrew(crewId);
        MemberCrew memberCrew = getMemberCrew(crew, userDetails.getMember());
        checkPermission(memberCrew);
        crew.update(crewRequestDto.getName(), crewRequestDto.getContent(), crewRequestDto.getImgUrl());
        return CrewResponseDto.from(crew);
    }

    private Member getMember(Member member) {
        return memberRepository.findById(member.getId()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private MemberCrew getMemberCrew(Crew crew, Member member) {
        return memberCrewRepository.findByCrewAndMember(crew, member).orElseThrow(()-> new CustomException(ErrorCode.MEMBERCREW_NOT_FOUND));
    }

    private Crew getCrew(Long crewId) {
        return crewRepository.findById(crewId).orElseThrow(()-> new CustomException(ErrorCode.CREW_NOT_FOUND));
    }

    private void checkPermission(MemberCrew memberCrew) {
        if (memberCrew.getStatus() == Status.SUBMIT) {
            throw new CustomException(ErrorCode.UPDATE_CREW_PERMISSION_ERROR);
        }
    }
}
