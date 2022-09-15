package com.project.crux.repository;

import com.project.crux.domain.Crew;
import com.project.crux.domain.Member;
import com.project.crux.domain.CrewMember;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@ActiveProfiles("prod")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CrewMemberRepositoryTest {
    static String CREW_NAME = "문어크루";
    static String CREW_CONTENT = "문어크루 입니다.";
    static String CREW_IMG_URL = "https://img.freepik.com/premium-vector/cute-octopus-cartoon-hand-drawn-style_42349-125.jpg?w=826";
    static String USER_EMAIL = "email.gmail.com";
    static String USER_NICKNAME = "nickname1";
    static String USER_PASSWORD = "password1";
    static String USER_CONTENT = "안녕하세요";
    static Crew crew;
    static Member member;

    @Autowired
    private CrewMemberRepository crewMemberRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CrewRepository crewRepository;

    @BeforeAll
    static void beforeAll() {
        crew = new Crew(CREW_NAME, CREW_CONTENT, CREW_IMG_URL);
        member = new Member(USER_EMAIL, USER_NICKNAME, USER_PASSWORD, USER_CONTENT, "이미지 주소");
    }

    @Transactional
    @DisplayName("멤버 크루 저장 성공")
    @Test
    void crewMemberSave() {
        //given
        Crew savedCrew = crewRepository.save(crew);
        Member savedMember = memberRepository.save(member);
        CrewMember crewMember = new CrewMember(savedMember, savedCrew);

        //when
        CrewMember savedCrewMember = crewMemberRepository.save(crewMember);

        //then
        Assertions.assertThat(savedCrewMember.getMember()).isSameAs(savedMember);
        Assertions.assertThat(savedCrewMember.getCrew()).isSameAs(savedCrew);
    }
}
