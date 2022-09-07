package com.project.crux.service;

import com.project.crux.domain.Crew;
import com.project.crux.domain.Member;
import com.project.crux.domain.MemberCrew;
import com.project.crux.domain.request.CrewRequestDto;
import com.project.crux.domain.response.CrewResponseDto;
import com.project.crux.exception.ErrorCode;
import com.project.crux.repository.CrewRepository;
import com.project.crux.repository.MemberCrewRepository;
import com.project.crux.repository.MemberRepository;
import com.project.crux.security.jwt.UserDetailsImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.*;

@ExtendWith(MockitoExtension.class)
class CrewServiceTest {
    static final String CREW_NAME = "문어크루";
    static final String CREW_CONTENT = "문어크루 입니다.";
    static final String CREW_IMG_URL = "https://img.freepik.com/premium-vector/cute-octopus-cartoon-hand-drawn-style_42349-125.jpg?w=826";
    static final String USER_EMAIL = "email.gmail.com";
    static final String USER_NICKNAME = "nickname1";
    static final String USER_PASSWORD = "password1";
    static final String USER_CONTENT = "안녕하세요";

    @InjectMocks
    private CrewService crewService;

    @Mock
    private CrewRepository crewRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberCrewRepository memberCrewRepository;

    @Nested
    @DisplayName("크루 생성")
    class createCrew {
        @DisplayName("크루 생성 성공")
        @Test
        void createCrewSuccess() {
            //given
            CrewRequestDto crewRequestDto = crewRequestDto();
            UserDetailsImpl userDetails = userDetails();

            when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(getSavedMember()));
            when(crewRepository.save(any(Crew.class))).thenReturn(getSavedCrew());
            when(memberCrewRepository.save(any(MemberCrew.class))).thenReturn(null);

            //when
            CrewResponseDto crewResponseDto = crewService.createCrew(crewRequestDto, userDetails);

            //then
            assertThat(crewResponseDto.getId()).isEqualTo(1L);
            assertThat(crewResponseDto.getName()).isEqualTo(CREW_NAME);
            assertThat(crewResponseDto.getContent()).isEqualTo(CREW_CONTENT);
            assertThat(crewResponseDto.getImgUrl()).isEqualTo(CREW_IMG_URL);
            assertThat(crewResponseDto.getCrewNum()).isEqualTo(1);
        }


        @Nested
        @DisplayName("크루 생성 실패")
        class createCrewFail {
            @DisplayName("크루 이름을 입력 해주세요")
            @Test
            void invalidCrewName() {
                //given
                CrewRequestDto crewRequestDto = crewRequestDto();
                setField(crewRequestDto, "name", " ");

                //when, then
                Assertions.assertThatThrownBy(() -> new Crew(crewRequestDto.getName(), crewRequestDto.getContent(), crewRequestDto.getImgUrl()))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage(ErrorCode.INVALID_CREW_NAME.getErrorMessage());
            }
        }
    }

    private Crew getSavedCrew() {
        Crew crew = new Crew(CREW_NAME, CREW_CONTENT, CREW_IMG_URL);
        setField(crew, "id", 1L);
        return crew;
    }


    private CrewRequestDto crewRequestDto() {
        return CrewRequestDto.builder()
                .name(CREW_NAME)
                .content(CREW_CONTENT)
                .imgUrl(CREW_IMG_URL)
                .build();
    }

    private UserDetailsImpl userDetails() {
        return new UserDetailsImpl(getSavedMember());
    }

    private Member getSavedMember() {
        Member member = Member.builder()
                .email(USER_EMAIL)
                .nickname(USER_NICKNAME)
                .password(USER_PASSWORD)
                .content(USER_CONTENT)
                .build();
        setField(member, "id", 1L);
        return member;
    }
}
