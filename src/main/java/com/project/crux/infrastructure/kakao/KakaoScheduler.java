package com.project.crux.infrastructure.kakao;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class KakaoScheduler {

    private final KakaoApiService kakaoApiService;

    @Scheduled(cron = "0 0 1 * * *")  // 매일 1시 마다 실행
    public void updateGymsPerDay() {
        double start_x = 126;
        double end_x = 131;
        double start_y = 33;
        double end_y = 39;

        kakaoApiService.updateGymByKakaoApi(start_x,start_y,end_x,end_y);
        kakaoApiService.updateGymImgByKakaoApi();
    }

}
