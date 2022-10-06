package com.project.crux.crew.service;

import com.project.crux.crew.Status;
import com.project.crux.crew.domain.*;
import com.project.crux.crew.repository.LikeCrewRepository;
import com.project.crux.crew.repository.CrewMemberRepository;
import com.project.crux.crew.repository.CrewPhotoRepository;
import com.project.crux.crew.repository.CrewPostRepository;
import com.project.crux.crew.repository.CrewRepository;
import com.project.crux.crew.domain.request.CrewPhotoRequestDto;
import com.project.crux.crew.domain.response.CrewMemberResponseDto;
import com.project.crux.crew.domain.response.CrewPostResponseDto;
import com.project.crux.exception.CustomException;
import com.project.crux.exception.ErrorCode;
import com.project.crux.member.domain.Member;
import com.project.crux.member.repository.MemberRepository;
import com.project.crux.security.jwt.UserDetailsImpl;
import com.project.crux.sse.domain.NotificationContent;
import com.project.crux.sse.service.NotificationService;
import com.project.crux.sse.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
    private final NotificationService notificationService;
    private final LikeCrewRepository likeCrewRepository;
    private final NoticeService noticeService;


    public String registerSubmit(Long crewId, UserDetailsImpl userDetails) {

        Crew crew = getCrew(crewId);
        Member member = userDetails.getMember();
        CrewMember crewMember = new CrewMember(member, crew);
        CrewMember crewLeader = getCrewLeader(crew);

        Optional<CrewMember> optionalCrewMember = crewMemberRepository.findByCrewAndMember(crew, member);
        if(!optionalCrewMember.isPresent()){
            checkAdminRegister(crewLeader, member);
            String content = member.getNickname() + "님이 가입 신청하셨습니다";
            sendNotice((crewLeader.getMember()), NotificationType.SUBMIT, new NotificationContent(crew.getId(), content));

            crewMemberRepository.save(crewMember);
            return "크루 가입 신청 완료";
        }

        CrewMember findCrewMember = optionalCrewMember.get();
        checkRegister(findCrewMember);
        String content = member.getNickname() + "님이 가입 취소하셨습니다";
        sendNotice((crewLeader.getMember()), NotificationType.CANCEL, new NotificationContent(crew.getId(), content));
        crewMemberRepository.delete(findCrewMember);
        return "크루 가입 신청 취소 완료";

    }


    public String registerPermit(UserDetailsImpl userDetails, Long crewId, Long memberId, Boolean permit) {

        Crew crew = getCrew(crewId);
        Member loginMember = userDetails.getMember();
        checkCrewLeader(getCrewMember(crew,loginMember));

        Member registerMember = getMember(memberId);
        CrewMember crewMember = getCrewMember(crew, registerMember);

        if (permit) {
            crewMember.updateStatus(Status.PERMIT);

            String content = registerMember.getNickname() + "님이 가입 되셨습니다";
            sendNotice(registerMember, NotificationType.PERMIT, new NotificationContent(crew.getId(), content));

            return "크루 가입 승인 완료";
        }

        crewMemberRepository.delete(crewMember);

        String content = registerMember.getNickname() + "님이 가입 거절되셨습니다";
        sendNotice(registerMember, NotificationType.REJECT, new NotificationContent(crew.getId(), content));

        return "크루 가입 승인 거절";
    }

    public List<CrewMemberResponseDto> findSummitCrewMembers(Long crewId, UserDetailsImpl userDetails) {
        Crew crew = getCrew(crewId);
        CrewMember crewMember = crewService.getCrewMember(crew, userDetails.getMember());
        checkAdmin(crewMember);
        return crewMemberRepository.findAllByCrewId(crew.getId()).stream()
                .filter(cm -> cm.getStatus().equals(Status.SUBMIT))
                .map(CrewMemberResponseDto::new)
                .collect(Collectors.toList());
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
        if (crewPhotoRequestDto.getImgList() == null) {
            throw new CustomException(ErrorCode.IMAGE_REQUIRED);
        }
        crewPhotoRequestDto.getImgList().forEach(imgUrl -> crewPhotoRepository.save(new CrewPhoto(crewPost, imgUrl)));
        String content = userDetails.getMember().getNickname() + "님이 사진 업로드했습니다";
        noticeService.sendNotice(crew, new NotificationContent(crew.getId(), content), userDetails.getMember());
        return new CrewPostResponseDto(crewPost);
    }

    @Transactional(readOnly = true)
    public List<CrewPostResponseDto> findAllCrewPosts(Long crewId, Pageable pageable) {
        return crewPostRepository.findAllByCrewMember_CrewId(crewId, pageable)
                .stream().map(CrewPostResponseDto::new).collect(Collectors.toList());
    }

    public String delete(Long postId, UserDetailsImpl userDetails) {
        CrewPost crewPost = crewPostRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        Member author = crewPost.getCrewMember().getMember();
        checkSameMember(author, userDetails.getMember());
        crewPostRepository.delete(crewPost);
        return "사진 삭제 완료";
    }

    public String like(Long crewId, UserDetailsImpl userDetails) {
        Crew crew = getCrew(crewId);
        Member member = userDetails.getMember();
        if (likeCrewRepository.findByCrewAndMember(crew, member).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_LIKED_CREW_EXCEPTION);
        }
        likeCrewRepository.save(new LikeCrew(crew, member));
        return "좋아요 완료";
    }

    public String unLike(Long crewId, UserDetailsImpl userDetails) {
        Crew crew = getCrew(crewId);
        Member member = userDetails.getMember();
        Optional<LikeCrew> likeCrew = likeCrewRepository.findByCrewAndMember(crew, member);
        if (!likeCrew.isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_UNLIKED_CREW_EXCEPTION);
        }
        likeCrewRepository.delete(likeCrew.get());
        return "좋아요 삭제 완료";
    }

    private Crew getCrew(Long crewId) {
        return crewRepository.findById(crewId).orElseThrow(() -> new CustomException(ErrorCode.CREW_NOT_FOUND));
    }
    private static void checkSameMember(Member author, Member loginMember) {
        if (!Objects.equals(author.getId(), loginMember.getId())) {
            throw new CustomException(ErrorCode.NO_PERMISSION_EXCEPTION);
        }
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

    private CrewMember getCrewMember(Crew crew, Member member) {
        return crewMemberRepository.findByCrewAndMember(crew, member).orElseThrow(() -> new CustomException(ErrorCode.CREWMEMBER_NOT_FOUND));
    }

    private CrewMember getCrewLeader(Crew crew) {
        return crew.getCrewMemberList().stream()
                .filter(cm -> cm.getStatus().equals(Status.ADMIN))
                .findAny().orElseThrow(() -> new CustomException(ErrorCode.CREWLEADER_NOT_FOUND));
    }

    private void checkCrewLeader(CrewMember crewLeader) {
        if (!(crewLeader.getStatus().equals(Status.ADMIN))) {
            throw new CustomException(ErrorCode.NOT_ADMIN);
        }
    }

    private void checkAdminRegister(CrewMember crewLeader, Member member) {
        if (crewLeader.getMember().equals(member)) {
            throw new CustomException(ErrorCode.ADMIN_REGISTER_SUBMIT);
        }
    }

    private void checkRegister(CrewMember crewMember) {
        if (crewMember.getStatus().equals(Status.ADMIN) || crewMember.getStatus().equals(Status.PERMIT)) {
            throw new CustomException(ErrorCode.REGISTER_EXIST);
        }
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private void sendNotice(Member member, NotificationType notificationType, NotificationContent content) {
        notificationService.send(member,notificationType,content);
    }

    public List<CrewPostResponseDto> findAllCrewsPosts(Pageable pageable) {
        return crewPostRepository.findAll(pageable).map(CrewPostResponseDto::new).toList();
    }
}
