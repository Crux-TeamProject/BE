package com.project.crux.service;

import com.project.crux.common.Status;
import com.project.crux.domain.Crew;
import com.project.crux.domain.CrewMember;
import com.project.crux.domain.Member;
import com.project.crux.domain.Notice;
import com.project.crux.domain.request.NoticeRequestDto;
import com.project.crux.domain.response.NoticeResponseDto;
import com.project.crux.exception.CustomException;
import com.project.crux.exception.ErrorCode;
import com.project.crux.repository.CrewMemberRepository;
import com.project.crux.repository.CrewRepository;
import com.project.crux.repository.NoticeRepository;
import com.project.crux.security.jwt.UserDetailsImpl;
import com.project.crux.sse.NotificationService;
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
    public NoticeResponseDto createNotice(Long crewId, NoticeRequestDto requestDto, UserDetailsImpl userDetails) {

        Notice notice = new Notice(requestDto.getContent());
        Crew crew = crewRepository.findById(crewId).orElseThrow(() -> new CustomException(ErrorCode.CREW_NOT_FOUND));
        Member member = userDetails.getMember();
        CrewMember crewMember = crewMemberRepository.findByCrewAndMember(crew, member).orElseThrow(() -> new CustomException(ErrorCode.CREWMEMBER_NOT_FOUND));

        if (crewMember.getStatus().equals(Status.SUBMIT)) {
            throw new CustomException(ErrorCode.NOT_ADMIN_OR_PERMIT);
        }
        notice.setCrewMember(crewMember);

        String content = member.getNickname() + "님이 공지사항을 올렸습니다.";
        crew.getCrewMemberList().stream().filter(cm -> cm.getStatus() == Status.ADMIN || cm.getStatus() == Status.PERMIT &&
                        !cm.getMember().equals(member))
                .forEach(cm -> {
                    notificationService.send(cm.getMember(), NotificationType.NOTICE, content);
                });

        noticeRepository.save(notice);
        return new NoticeResponseDto(notice);
    }

    @Transactional
    public NoticeResponseDto updateNotice(Long noticeId, NoticeRequestDto requestDto, UserDetailsImpl userDetails) {

        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() -> new CustomException(ErrorCode.NOTICE_NOT_FOUND));

        Member member = userDetails.getMember();

        if (!notice.getCrewMember().getMember().equals(member)) {
            throw new CustomException(ErrorCode.INVALID_NOTICE_UPDATE);
        }

        notice.update(requestDto.getContent());

        return new NoticeResponseDto(notice);
    }

    public String deleteNotice(Long noticeId, UserDetailsImpl userDetails) {

        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() -> new CustomException(ErrorCode.NOTICE_NOT_FOUND));

        Member member = userDetails.getMember();

        if (!notice.getCrewMember().getMember().equals(member)) {
            throw new CustomException(ErrorCode.INVALID_NOTICE_DELETE);
        }
        noticeRepository.delete(notice);

        return "공지사항 삭제 완료";
    }
}
