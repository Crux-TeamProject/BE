package com.project.crux.infrastructure.kakao;

import com.project.crux.gym.repository.GymRepository;
import com.project.crux.gym.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class Scheduler {

    private final KakaoApiService kakaoApiService;
    private final ReviewRepository reviewRepository;
    private final GymRepository gymRepository;

    @Scheduled(cron = "0 0 5 * * *")  // 매일 5시 마다 실행
    public void updateGymsPerDay() {
        double start_x = 126;
        double end_x = 131;
        double start_y = 33;
        double end_y = 39;

        kakaoApiService.updateGymByKakaoApi(start_x,start_y,end_x,end_y);
        //        kakaoApiService.updateGymImgByKakaoApi();

        gymRepository.findAll().stream()
                .filter(gym -> !reviewRepository.findByGymOrderByIdDesc(gym).isEmpty())
                .forEach(gym -> {
                    gym.updateImg(reviewRepository.findByGymOrderByIdDesc(gym)
                            .get(0).getReviewPhotoList().get(0).getImgUrl());
                });
    }

}
