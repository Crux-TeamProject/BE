package com.project.crux.infrastructure.kakao;

import com.project.crux.gym.domain.ReviewPhoto;
import com.project.crux.gym.repository.GymRepository;
import com.project.crux.gym.repository.ReviewPhotoRepository;
import com.project.crux.gym.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Component
@RequiredArgsConstructor
public class InitialDataRunner implements ApplicationRunner {


    private final KakaoApiService kakaoApiService;
    private final GymRepository gymRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewPhotoRepository reviewPhotoRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {

        double start_x = 126;
        double end_x = 131;
        double start_y = 33;
        double end_y = 39;

        kakaoApiService.updateGymByKakaoApi(start_x, start_y, end_x, end_y);
//        kakaoApiService.updateGymImgByKakaoApi();

        gymRepository.findAll().stream()
                .filter(gym -> !reviewRepository.findByGymOrderByIdDesc(gym).isEmpty())
                .forEach(gym -> {
                    List<ReviewPhoto> reviewPhotoList = reviewPhotoRepository.findAllByReview(
                            reviewRepository.findByGymOrderByIdDesc(gym).get(0));
                    if (!reviewPhotoList.isEmpty()) {
                        gym.updateImg(reviewPhotoList.get(0).getImgUrl());}
                });

    }

}