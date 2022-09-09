package com.project.crux.service;

import com.project.crux.common.Status;
import com.project.crux.domain.Crew;
import com.project.crux.domain.Member;
import com.project.crux.domain.MemberCrew;
import com.project.crux.exception.CustomException;
import com.project.crux.exception.ErrorCode;
import com.project.crux.repository.CrewRepository;
import com.project.crux.repository.MemberCrewRepository;
import com.project.crux.repository.MemberRepository;
import com.project.crux.security.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberCrewService {

    private final CrewRepository crewRepository;
    private final MemberCrewRepository memberCrewRepository;
    private final MemberRepository memberRepository;
    private final CrewService crewService;


    public String registerSubmit(Long crewId, UserDetailsImpl userDetails) {

        Crew crew = crewRepository.findById(crewId).orElseThrow(()-> new CustomException(ErrorCode.CREW_NOT_FOUND));
        Member member = userDetails.getMember();

        if(memberCrewRepository.findByCrewAndMember(crew,member).isPresent()){
            throw new CustomException(ErrorCode.ADMIN_REGISTER_SUBMIT);
        }

        MemberCrew memberCrew = new MemberCrew(member, crew);
        memberCrewRepository.save(memberCrew);
        return "크루 가입 신청 완료";
    }

    public String registerPermit(Long crewId, Long memberId) {

        Crew crew = crewRepository.findById(crewId).orElseThrow(()-> new CustomException(ErrorCode.CREW_NOT_FOUND));
        Member member = memberRepository.findById(memberId).orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        MemberCrew memberCrew = memberCrewRepository.findByCrewAndMember(crew,member).orElseThrow(()-> new CustomException(ErrorCode.MEMBERCREW_NOT_FOUND));
        memberCrew.updateStatus(Status.PERMIT);
        return "크루 가입 승인 완료";
    }

    public String withdrawCrew(Long crewId, UserDetailsImpl userDetails) {
        Crew crew = crewService.getCrew(crewId);
        MemberCrew memberCrew = crewService.getMemberCrew(crew, userDetails.getMember());
        checkAdminOrPermit(memberCrew);
        memberCrewRepository.delete(memberCrew);
        return "크루 탈퇴 완료";
    }

    public String dropMemberCrew(Long crewId, Long memberId, UserDetailsImpl userDetails) {
        Crew crew = crewService.getCrew(crewId);
        Member toMember = crewService.getMember(memberId);
        MemberCrew toMemberCrew = crewService.getMemberCrew(crew, toMember);
        MemberCrew fromMemberCrew = crewService.getMemberCrew(crew, userDetails.getMember());
        checkAdmin(fromMemberCrew);
        checkPermit(toMemberCrew);
        memberCrewRepository.delete(toMemberCrew);
        return "크루 추방 완료";
    }

    private void checkPermit(MemberCrew memberCrew) {
        if (memberCrew.getStatus() != Status.PERMIT) {
            throw new CustomException(ErrorCode.NOT_PERMIT);
        }
    }

    private void checkAdminOrPermit(MemberCrew memberCrew) {
        if (memberCrew.getStatus() == Status.SUBMIT) {
            throw new CustomException(ErrorCode.NOT_ADMIN_OR_PERMIT);
        }
    }

    private void checkAdmin(MemberCrew memberCrew) {
        if (memberCrew.getStatus() != Status.ADMIN) {
            throw new CustomException(ErrorCode.NOT_ADMIN);
        }
    }
}
