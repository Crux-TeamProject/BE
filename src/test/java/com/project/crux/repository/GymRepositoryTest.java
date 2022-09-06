package com.project.crux.repository;

import com.project.crux.domain.Gym;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GymRepositoryTest {

    @Autowired
    private GymRepository gymRepository;

    static List<Gym> gyms = new ArrayList<>();

    @BeforeAll
    static void initData() {
        for (double i = 0; i < 5; i += 0.25) {
            Gym gym = new Gym("클라이밍짐" + i, "주소" + i, "전화번호" + i, i);
            gyms.add(gym);
        }
    }

    @Test
    @DisplayName("클라이밍짐 - 이름으로 조회 성공")
    void findByName() {

        //given
        double i = 3;
        gymRepository.saveAll(gyms);

        //when
        Optional<Gym> optionalGym = gymRepository.findByName("클라이밍짐" + i);

        //then
        optionalGym.ifPresent(gym -> {
            assertThat(gym.getName()).isEqualTo("클라이밍짐" + i);
            assertThat(gym.getLocation()).isEqualTo("주소" + i);
            assertThat(gym.getPhone()).isEqualTo("전화번호" + i);
            assertThat(gym.getAvgScore()).isEqualTo(i);
        });
    }

    @Test
    @DisplayName("인기클라이밍짐 - 평균평점 미만 조회 성공")
    void findByAvgScoreLessThan() {

        //given
        double lastAvgScore = 3.5;
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by("avgScore").descending());
        gymRepository.saveAll(gyms);

        //when
        Page<Gym> gymPage = gymRepository.findByAvgScoreLessThan(lastAvgScore, pageRequest);

        //then
        assertThat(gymPage.getContent().stream().allMatch(gym -> gym.getAvgScore() < lastAvgScore)).isEqualTo(true);
    }

    @Test
    @Order(2)
    @DisplayName("클라이밍짐 - ID미만 검색 조회 성공")
    void findByIdLessThanAndNameContains() {

        //given
        long lastArticleId = 20L;
        String query = "클라이밍";
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by("id").descending());
        gymRepository.saveAll(gyms);

        //when
        Page<Gym> gymPage = gymRepository.findByIdLessThanAndNameContains(lastArticleId, query, pageRequest);

        //then
        assertThat(gymPage.getContent().stream().allMatch(gym -> gym.getId() < lastArticleId)).isEqualTo(true);
    }

    @Test
    @Order(1)
    @DisplayName("클라이밍짐 - ID 상세 조회 성공")
    void findById() {

        //given
        Long gymId = 3L;
        gymRepository.saveAll(gyms);
        //when
        Optional<Gym> gym = gymRepository.findById(gymId);

        //then
        assertThat(gym.get().getName()).isEqualTo("클라이밍짐"+0.25 * (gymId-1));
        assertThat(gym.get().getLocation()).isEqualTo("주소"+0.25 * (gymId-1));
        assertThat(gym.get().getPhone()).isEqualTo("전화번호"+0.25 * (gymId-1));
        assertThat(gym.get().getAvgScore()).isEqualTo(0.25 * (gymId-1));
    }
}