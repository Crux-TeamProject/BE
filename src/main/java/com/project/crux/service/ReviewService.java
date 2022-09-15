package com.project.crux.service;

import com.project.crux.domain.Gym;
import com.project.crux.domain.Member;
import com.project.crux.domain.Review;
import com.project.crux.domain.ReviewPhoto;
import com.project.crux.domain.request.ReviewRequestDto;
import com.project.crux.domain.response.ReviewPhotoResponseDto;
import com.project.crux.domain.response.ReviewResponseDto;
import com.project.crux.exception.CustomException;
import com.project.crux.exception.ErrorCode;
import com.project.crux.repository.GymRepository;
import com.project.crux.repository.ReviewPhotoRepository;
import com.project.crux.repository.ReviewRepository;
import com.project.crux.security.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final GymRepository gymRepository;
    private final ReviewPhotoRepository reviewPhotoRepository;

    @Transactional
    public ReviewResponseDto createReview(ReviewRequestDto requestDto, Long gymId, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();
        Gym gym = gymRepository.findById(gymId).orElseThrow(() -> new CustomException(ErrorCode.GYM_NOT_FOUND));

        Review review = Review.builder()
                .score(requestDto.getScore())
                .content(requestDto.getContent())
                .member(member)
                .gym(gym)
                .build();

       gym.updateScore(requestDto.getScore());

        reviewRepository.save(review);
        ReviewResponseDto reviewResponseDto = new ReviewResponseDto(review);

        imgSave(requestDto, reviewResponseDto, review);

        return reviewResponseDto;
    }

    @Transactional
    public ReviewResponseDto updateReview(ReviewRequestDto requestDto, Long reviewId,
                                          UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        if (!review.getMember().equals(member)) {
            throw new CustomException(ErrorCode.INVALID_REVIEW_UPDATE);
        }

        review.update(requestDto.getScore(), requestDto.getContent());
        reviewPhotoRepository.deleteAll(reviewPhotoRepository.findAllByReview(review));
        ReviewResponseDto reviewResponseDto = new ReviewResponseDto(review);

        imgSave(requestDto, reviewResponseDto, review);

        return reviewResponseDto;
    }

    public String deleteReview(UserDetailsImpl userDetails, Long reviewId) {
        Member member = userDetails.getMember();
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        if (!review.getMember().equals(member)) {
            throw new CustomException(ErrorCode.INVALID_REVIEW_DELETE);
        }
        reviewRepository.delete(review);
        return "후기 삭제 완료";
    }

    public ReviewResponseDto getReview(Long reviewId) {

        Review review = reviewRepository.findById(reviewId).orElseThrow(()-> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
        Member member = review.getMember();
        ReviewResponseDto reviewResponseDto = new ReviewResponseDto(member,review);
        List<ReviewPhoto> reviewPhotoList = reviewPhotoRepository.findAllByReview(review);

        reviewPhotoList.forEach(reviewPhoto -> reviewResponseDto.getReviewPhotoList().add(new ReviewPhotoResponseDto(reviewPhoto)));
        return reviewResponseDto;
    }


    private void imgSave(ReviewRequestDto requestDto, ReviewResponseDto reviewResponseDto, Review review) {
        requestDto.getReviewPhotoList().forEach(reviewPhoto -> {
            reviewPhoto.setReview(review);
            reviewPhotoRepository.save(reviewPhoto);
            reviewResponseDto.getReviewPhotoList().add(new ReviewPhotoResponseDto(reviewPhoto.getImgUrl()));
        });
    }

}
