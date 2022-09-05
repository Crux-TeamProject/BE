package com.project.crux.service;

import com.project.crux.domain.Gym;
import com.project.crux.domain.response.GymResponseDto;
import com.project.crux.exception.CustomException;
import com.project.crux.repository.GymRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class GymServiceTest {

    @InjectMocks
    private GymService gymService;

    @Mock
    private GymRepository gymRepository;

    static Page<Gym> gymPage;

    @BeforeAll
    static void initData() {
        List<Gym> gyms = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Gym gym = new Gym("클라이밍짐", "주소", "전화번호", i);
            gyms.add(gym);
        }
        gymPage = new PageImpl<>(gyms);
    }

    @Test
    @DisplayName("인기클라이밍짐 - 조회 성공")
    void getPopularGyms() {

        //given
        double lastAvgScore = 5;
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by("avgScore").descending());
        when(gymRepository.findByAvgScoreLessThan(lastAvgScore, pageRequest)).thenReturn(gymPage);

        //when
        final List<GymResponseDto> gymResponseDtos = gymService.getPopularGyms(lastAvgScore, 5);

        //then
        assertThat(gymResponseDtos.size()).isEqualTo(5);
    }

    @Test
    @DisplayName("인기클라이밍짐 - 평점 범위 밖으로 설정")
    void getPopularGyms_failed() {

        //given
        double lastAvgScore = 7;

        //when
        CustomException exception = Assertions.assertThrows(CustomException.class,
                () -> gymService.getPopularGyms(lastAvgScore, 5));

        //then
        assertThat("평균 평점은 0에서 5사이여야 합니다").isEqualTo(exception.getErrorCode().getErrorMessage());
    }



}