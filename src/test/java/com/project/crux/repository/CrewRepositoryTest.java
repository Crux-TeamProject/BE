package com.project.crux.repository;

import com.project.crux.domain.Crew;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Transactional
@ActiveProfiles("prod")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CrewRepositoryTest {
    static String CREW_NAME = "문어크루";
    static String CREW_CONTENT = "문어크루 입니다.";
    static String CREW_IMG_URL = "https://img.freepik.com/premium-vector/cute-octopus-cartoon-hand-drawn-style_42349-125.jpg?w=826";

    @Autowired
    private CrewRepository crewRepository;

    @DisplayName("크루 저장 성공")
    @Test
    void crewSave() {
        //when
        Crew savedCrew = crewRepository.save(new Crew(CREW_NAME, CREW_CONTENT, CREW_IMG_URL));
        //then
        Assertions.assertThat(savedCrew.getName()).isEqualTo(CREW_NAME);
        Assertions.assertThat(savedCrew.getContent()).isEqualTo(CREW_CONTENT);
        Assertions.assertThat(savedCrew.getImgUrl()).isEqualTo(CREW_IMG_URL);
    }

//    @Test
//    @DisplayName("전체 크루 조회 성공")
//    void findByIdLessThanOrderByIdDesc() {
//        //given
//        long lastCrewId = 4L;
//        int size = 2;
//        crewRepository.save(new Crew(CREW_NAME, CREW_CONTENT, CREW_IMG_URL));
//        crewRepository.save(new Crew(CREW_NAME, CREW_CONTENT, CREW_IMG_URL));
//        crewRepository.save(new Crew(CREW_NAME, CREW_CONTENT, CREW_IMG_URL));
//        crewRepository.save(new Crew(CREW_NAME, CREW_CONTENT, CREW_IMG_URL));
//
//        PageRequest pageRequest = PageRequest.of(0, size);
//
//        //when
//        Page<Crew> crews = crewRepository.findByIdLessThanOrderByIdDesc(lastCrewId, pageRequest);
//
//        //then
//        assertThat(crews.getContent().size()).isEqualTo(size);
//    }
}