package com.project.crux.service;

import com.project.crux.common.Status;
import com.project.crux.domain.*;
import com.project.crux.domain.request.CrewRequestDto;
import com.project.crux.domain.response.CrewFindOneResponseDto;
import com.project.crux.domain.response.CrewResponseDto;
import com.project.crux.exception.CustomException;
import com.project.crux.exception.ErrorCode;
import com.project.crux.repository.*;
import com.project.crux.security.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CrewService {

    private static final String DEFAULT_IMAGE_URL = "";

    private final CrewRepository crewRepository;
    private final CrewMemberRepository crewMemberRepository;
    private final MemberRepository memberRepository;
    private final NoticeRepository noticeRepository;
    private final LikeCrewRepository likeCrewRepository;

    public CrewResponseDto createCrew(CrewRequestDto crewRequestDto, UserDetailsImpl userDetails) {
        Member member = getMember(userDetails.getMember().getId());
        String imgUrl = getImgUrl(crewRequestDto);
        Crew savedCrew = crewRepository.save(new Crew(crewRequestDto.getName(), crewRequestDto.getContent(), imgUrl));

        CrewMember crewMember = new CrewMember(member, savedCrew);
        crewMember.updateStatus(Status.ADMIN);
        crewMemberRepository.save(crewMember);

        return CrewResponseDto.from(savedCrew);
    }

    private String getImgUrl(CrewRequestDto crewRequestDto) {
        return crewRequestDto.getImgUrl() == null ? DEFAULT_IMAGE_URL : crewRequestDto.getImgUrl();
    }

    @Transactional(readOnly = true)
    public Page<CrewResponseDto> findAllCrew(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getCredentials().equals("")) {
            return crewRepository.findAll(pageable).map(CrewResponseDto::from);
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Member member = userDetails.getMember();
        return crewRepository.findAll(pageable).map(crew -> {
            Optional<LikeCrew> likeCrew = likeCrewRepository.findByCrewAndMember(crew, member);
            return CrewResponseDto.of(crew, likeCrew.isPresent());
        });
    }

    @Transactional(readOnly = true)
    public List<CrewResponseDto> searchCrew(String query) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getCredentials().equals("")) {
            return crewRepository.findAllByNameContainsIgnoreCase(query).stream()
                    .map(CrewResponseDto::from).collect(Collectors.toList());
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Member member = userDetails.getMember();
        return crewRepository.findAllByNameContainsIgnoreCase(query).stream()
                .map(crew -> {
                    Optional<LikeCrew> likeCrew = likeCrewRepository.findByCrewAndMember(crew, member);
                    return CrewResponseDto.of(crew, likeCrew.isPresent());})
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CrewFindOneResponseDto findCrew(Long crewId) {
        Crew crew = getCrew(crewId);
        List<Notice> noticeList = noticeRepository.findAllByCrewMember_Crew(crew);
        return new CrewFindOneResponseDto(crew, noticeList);
    }

    public CrewResponseDto updateCrew(Long crewId, CrewRequestDto crewRequestDto, UserDetailsImpl userDetails) {
        Crew crew = getCrew(crewId);
        CrewMember crewMember = getCrewMember(crew, userDetails.getMember());
        checkAdmin(crewMember);
        crew.update(crewRequestDto.getName(), crewRequestDto.getContent(), crewRequestDto.getImgUrl());
        return CrewResponseDto.from(crew);
    }

    public String deleteCrew(Long crewId, UserDetailsImpl userDetails) {
        Crew crew = getCrew(crewId);
        CrewMember crewMember = getCrewMember(crew, userDetails.getMember());
        checkAdmin(crewMember);
        crewRepository.delete(crew);
        return "크루 삭제 완료";
    }

    Member getMember(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    CrewMember getCrewMember(Crew crew, Member member) {
        return crewMemberRepository.findByCrewAndMember(crew, member).orElseThrow(()-> new CustomException(ErrorCode.CREWMEMBER_NOT_FOUND));
    }

    Crew getCrew(Long crewId) {
        return crewRepository.findById(crewId).orElseThrow(()-> new CustomException(ErrorCode.CREW_NOT_FOUND));
    }

    private void checkAdmin(CrewMember crewMember) {
        if (crewMember.getStatus() != Status.ADMIN) {
            throw new CustomException(ErrorCode.NOT_ADMIN);
        }
    }
}
