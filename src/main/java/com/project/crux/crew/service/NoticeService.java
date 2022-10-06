package com.project.crux.crew.service;

import com.project.crux.crew.Status;
import com.project.crux.crew.domain.Crew;
import com.project.crux.crew.domain.CrewMember;
import com.project.crux.crew.domain.response.CrewNoticeResponseDto;
import com.project.crux.member.domain.Member;
import com.project.crux.crew.domain.Notice;
import com.project.crux.crew.domain.request.NoticeRequestDto;
import com.project.crux.exception.CustomException;
import com.project.crux.exception.ErrorCode;
import com.project.crux.crew.repository.CrewMemberRepository;
import com.project.crux.crew.repository.CrewRepository;
import com.project.crux.crew.repository.NoticeRepository;
import com.project.crux.security.jwt.UserDetailsImpl;
import com.project.crux.sse.domain.NotificationContent;
import com.project.crux.sse.service.NotificationService;
import com.project.crux.sse.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final CrewRepository crewRepository;
    private final CrewMemberRepository crewMemberRepository;
    private final NotificationService notificationService;

    @Transactional
    public CrewNoticeResponseDto createNotice(Long crewId, NoticeRequestDto requestDto, UserDetailsImpl userDetails) {

        Notice notice = new Notice(requestDto.getDate(), requestDto.getPlace(), requestDto.getContent());
        Crew crew = getCrew(crewId);
        Member member = userDetails.getMember();
        CrewMember crewMember = getCrewMember(crew, member);

        checkAdminOrPermit(crewMember);

        notice.setCrewMember(crewMember);

        String content = member.getNickname() + "님이 공지사항을 올렸습니다.";
        sendNotice(crew, new NotificationContent(crew.getId(), content), member);

        noticeRepository.save(notice);
        return new CrewNoticeResponseDto(notice);
    }

    @Transactional
    public CrewNoticeResponseDto updateNotice(Long noticeId, NoticeRequestDto requestDto, UserDetailsImpl userDetails) {

        Notice notice = getNotice(noticeId);

        Member member = userDetails.getMember();

        noticeWriterCheck(notice,member,"update");

        notice.update(requestDto.getDate(),requestDto.getPlace(), requestDto.getContent());

        return new CrewNoticeResponseDto(notice);
    }

    public String deleteNotice(Long noticeId, UserDetailsImpl userDetails) {

        Notice notice = getNotice(noticeId);

        Member member = userDetails.getMember();

        noticeWriterCheck(notice,member,"delete");

        noticeRepository.delete(notice);

        return "공지사항 삭제 완료";
    }

    private Crew getCrew(Long crewId) {
        return crewRepository.findById(crewId).orElseThrow(()-> new CustomException(ErrorCode.CREW_NOT_FOUND));
    }

    private CrewMember getCrewMember(Crew crew, Member member) {
        return crewMemberRepository.findByCrewAndMember(crew, member).orElseThrow(()-> new CustomException(ErrorCode.CREWMEMBER_NOT_FOUND));
    }

    private void checkAdminOrPermit(CrewMember crewMember) {
        if (crewMember.getStatus() == Status.SUBMIT) {
            throw new CustomException(ErrorCode.NOT_ADMIN_OR_PERMIT);
        }
    }

    public void sendNotice(Crew crew, NotificationContent content ,Member member) {
        crew.getCrewMemberList().stream().filter(cm -> cm.getStatus() == Status.ADMIN || cm.getStatus() == Status.PERMIT &&
                        !cm.getMember().equals(member))
                .forEach(cm -> {
                    notificationService.send(cm.getMember(), NotificationType.NOTICE, content);
                });
    }

    private Notice getNotice(Long noticeId) {
        return noticeRepository.findById(noticeId).orElseThrow(() -> new CustomException(ErrorCode.NOTICE_NOT_FOUND));
    }

    private void noticeWriterCheck(Notice notice, Member member, String updateOrDelete) {
        if (!notice.getCrewMember().getMember().equals(member)) {
            if (updateOrDelete.equals("update")) {
                throw new CustomException(ErrorCode.INVALID_NOTICE_UPDATE);
            }
            throw new CustomException(ErrorCode.INVALID_NOTICE_DELETE);
        }
    }
}
