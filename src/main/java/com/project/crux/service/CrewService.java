package com.project.crux.service;

import com.project.crux.common.Status;
import com.project.crux.domain.Crew;
import com.project.crux.domain.Member;
import com.project.crux.domain.MemberCrew;
import com.project.crux.domain.request.CrewRequestDto;
import com.project.crux.domain.response.CrewResponseDto;
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

    private Member getMember(Member member) {
        return memberRepository.findById(member.getId()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

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

    public Page<CrewResponseDto> findAllPopularCrew(Pageable pageable) {
        return crewRepository.findAll(pageable).map(CrewResponseDto::from);
    }
}
