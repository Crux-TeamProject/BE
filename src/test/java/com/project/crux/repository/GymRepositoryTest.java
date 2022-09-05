package com.project.crux.repository;

import com.project.crux.domain.Gym;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
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
        Optional<Gym> optionalGym = gymRepository.findByName("클라이밍짐"+i);

        //then
        optionalGym.ifPresent(gym->{
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
}