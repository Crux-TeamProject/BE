package com.project.crux.service;

import com.project.crux.common.Status;
import com.project.crux.domain.*;
import com.project.crux.domain.request.CrewPhotoRequestDto;
import com.project.crux.domain.response.CrewPostResponseDto;
import com.project.crux.exception.CustomException;
import com.project.crux.exception.ErrorCode;
import com.project.crux.repository.*;
import com.project.crux.security.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CrewMemberService {

    private final CrewRepository crewRepository;
    private final CrewMemberRepository crewMemberRepository;
    private final MemberRepository memberRepository;
    private final CrewPostRepository crewPostRepository;
    private final CrewPhotoRepository crewPhotoRepository;
    private final CrewService crewService;


    public String registerSubmit(Long crewId, UserDetailsImpl userDetails) {

        Crew crew = crewRepository.findById(crewId).orElseThrow(() -> new CustomException(ErrorCode.CREW_NOT_FOUND));
        Member member = userDetails.getMember();

        if (crewMemberRepository.findByCrewAndMember(crew, member).isPresent()) {
            throw new CustomException(ErrorCode.ADMIN_REGISTER_SUBMIT);
        }

        CrewMember crewMember = new CrewMember(member, crew);
        crewMemberRepository.save(crewMember);
        return "크루 가입 신청 완료";
    }

    public String registerPermit(Long crewId, Long memberId) {

        Crew crew = crewRepository.findById(crewId).orElseThrow(() -> new CustomException(ErrorCode.CREW_NOT_FOUND));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        CrewMember crewMember = crewMemberRepository.findByCrewAndMember(crew, member).orElseThrow(() -> new CustomException(ErrorCode.MEMBERCREW_NOT_FOUND));
        crewMember.updateStatus(Status.PERMIT);
        return "크루 가입 승인 완료";
    }

    public String withdrawCrew(Long crewId, UserDetailsImpl userDetails) {
        Crew crew = crewService.getCrew(crewId);
        CrewMember crewMember = crewService.getCrewMember(crew, userDetails.getMember());
        checkAdminOrPermit(crewMember);
        crewMemberRepository.delete(crewMember);
        return "크루 탈퇴 완료";
    }

    public String dropMemberCrew(Long crewId, Long memberId, UserDetailsImpl userDetails) {
        Crew crew = crewService.getCrew(crewId);
        Member toMember = crewService.getMember(memberId);
        CrewMember toCrewMember = crewService.getCrewMember(crew, toMember);
        CrewMember fromCrewMember = crewService.getCrewMember(crew, userDetails.getMember());
        checkAdmin(fromCrewMember);
        checkPermit(toCrewMember);
        crewMemberRepository.delete(toCrewMember);
        return "크루 추방 완료";
    }

    public CrewPostResponseDto createCrewPost(Long crewId, CrewPhotoRequestDto crewPhotoRequestDto, UserDetailsImpl userDetails) {
        Crew crew = crewService.getCrew(crewId);
        CrewMember crewMember = crewService.getCrewMember(crew, userDetails.getMember());
        CrewPost crewPost = crewPostRepository.save(new CrewPost(crewMember));
        crewPhotoRequestDto.getImgList().forEach(imgUrl -> crewPhotoRepository.save(new CrewPhoto(crewPost, imgUrl)));
        return new CrewPostResponseDto(crewPost);
    }

    @Transactional(readOnly = true)
    public List<CrewPostResponseDto> findAllCrewPosts(Long crewId, Long lastCrewPostId, int size) {
        PageRequest pageRequest = PageRequest.of(0, size).withSort(Sort.Direction.DESC, "id");
        return crewPostRepository.findAllByIdLessThanAndCrewMember_CrewId(lastCrewPostId, crewId, pageRequest)
                .stream().map(CrewPostResponseDto::new).collect(Collectors.toList());
    }

    private void checkPermit(CrewMember crewMember) {
        if (crewMember.getStatus() != Status.PERMIT) {
            throw new CustomException(ErrorCode.NOT_PERMIT);
        }
    }

    private void checkAdminOrPermit(CrewMember crewMember) {
        if (crewMember.getStatus() == Status.SUBMIT) {
            throw new CustomException(ErrorCode.NOT_ADMIN_OR_PERMIT);
        }
    }

    private void checkAdmin(CrewMember crewMember) {
        if (crewMember.getStatus() != Status.ADMIN) {
            throw new CustomException(ErrorCode.NOT_ADMIN);
        }
    }
}
