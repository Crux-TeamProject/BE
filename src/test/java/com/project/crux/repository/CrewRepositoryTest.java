package com.project.crux.repository;

import com.project.crux.domain.Crew;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
class CrewRepositoryTest {
    static String CREW_NAME = "문어크루";
    static String CREW_CONTENT = "문어크루 입니다.";
    static String CREW_IMG_URL = "https://img.freepik.com/premium-vector/cute-octopus-cartoon-hand-drawn-style_42349-125.jpg?w=826";
    static Crew crew;

    @Autowired
    private CrewRepository crewRepository;

    @BeforeAll
    static void beforeAll() {
        crew = new Crew(CREW_NAME, CREW_CONTENT, CREW_IMG_URL);
    }

    @Transactional
    @DisplayName("크루 저장 성공")
    @Test
    void crewSave() {
        //when
        Crew savedCrew = crewRepository.save(crew);
        //then
        Assertions.assertThat(savedCrew.getName()).isEqualTo(crew.getName());
        Assertions.assertThat(savedCrew.getContent()).isEqualTo(crew.getContent());
        Assertions.assertThat(savedCrew.getImgUrl()).isEqualTo(crew.getImgUrl());
    }

}