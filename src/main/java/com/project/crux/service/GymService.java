package com.project.crux.service;

import com.project.crux.domain.*;
import com.project.crux.domain.response.GymResponseDto;
import com.project.crux.domain.response.ReviewPhotoResponseDto;
import com.project.crux.domain.response.ReviewResponseDto;
import com.project.crux.exception.CustomException;
import com.project.crux.exception.ErrorCode;
import com.project.crux.repository.GymRepository;
import com.project.crux.repository.LikeGymRepository;
import com.project.crux.repository.ReviewPhotoRepository;
import com.project.crux.repository.ReviewRepository;
import com.project.crux.security.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GymService {

    private final GymRepository gymRepository;
    private final LikeGymRepository likeGymRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewPhotoRepository reviewPhotoRepository;


    public List<GymResponseDto> getPopularGyms(Pageable pageable) {

        Page<Gym> gyms = gymRepository.findAll(pageable);

        return pageToDtoList(gyms);
    }


    public List<GymResponseDto> getSearchGyms(String query, Long lastArticleId, int size) {

        if (lastArticleId < 0 || Integer.MAX_VALUE < lastArticleId) {
            throw new CustomException(ErrorCode.INVALID_ARTICLEID);

        }
        PageRequest pageRequest = PageRequest.of(0, size, Sort.by("id").descending());

        Page<Gym> gyms = gymRepository.findByIdLessThanAndNameContains(lastArticleId, query, pageRequest);

        return pageToDtoList(gyms);
    }


    public GymResponseDto getGym(Long gymId) {

        Gym gym = gymRepository.findById(gymId).orElseThrow(() -> new CustomException(ErrorCode.GYM_NOT_FOUND));

        GymResponseDto gymResponseDto = new GymResponseDto(gym);
        List<Review> reviewList = reviewRepository.findByGym(gym);

        List<ReviewResponseDto> reviews = gymResponseDto.getReviews();

        reviewList.forEach(review -> {
            ReviewResponseDto reviewResponseDto = new ReviewResponseDto(review);
            List<ReviewPhoto> reviewPhotoList = reviewPhotoRepository.findAllByReview(review);
            reviewPhotoList.forEach(reviewPhoto -> reviewResponseDto.getReviewPhotoList().add(new ReviewPhotoResponseDto(reviewPhoto)));
            reviews.add(reviewResponseDto);
        });

        return gymResponseDto;
    }

    @Transactional
    public String likeGym(UserDetailsImpl userDetails, Long gymId) {

        Member member = userDetails.getMember();
        Gym gym = gymRepository.findById(gymId).orElseThrow(()->new CustomException(ErrorCode.GYM_NOT_FOUND));

        LikeGym existLike = likeGymRepository.findByMemberAndGymId(member, gymId);

        if (existLike == null) {
            LikeGym likeGym = new LikeGym(member,gym);
            likeGymRepository.save(likeGym);

            return "즐겨 찾기 추가 완료";
        }
        likeGymRepository.delete(existLike);

        return "즐겨 찾기 삭제 완료";
    }


    private List<GymResponseDto> pageToDtoList(Page<Gym> gyms) {

        List<GymResponseDto> gymResponseDtos = new ArrayList<>();

        gyms.getContent().forEach(gym -> gymResponseDtos.add(new GymResponseDto(gym)));

        return gymResponseDtos;
    }

}
