package com.project.crux.gym.controller;

import com.project.crux.gym.domain.request.ReviewRequestDto;
import com.project.crux.common.ResponseDto;
import com.project.crux.gym.domain.response.ReviewResponseDto;
import com.project.crux.security.jwt.UserDetailsImpl;
import com.project.crux.gym.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    //api 짐 리뷰 작성
    @PostMapping("/reviews/{gymId}")
    public ResponseDto<ReviewResponseDto> createReview(@Valid @RequestBody ReviewRequestDto requestDto, @PathVariable Long gymId,
                                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(reviewService.createReview(requestDto, gymId, userDetails));
    }

    //api 짐 리뷰 수정
    @PutMapping("/reviews/{reviewId}")
    public ResponseDto<ReviewResponseDto> updateReview(@RequestBody ReviewRequestDto requestDto, @PathVariable Long reviewId,
                                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(reviewService.updateReview(requestDto , reviewId ,userDetails));
    }

    //api 짐 리뷰 삭제
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseDto<?> deleteReview(@PathVariable Long reviewId,
                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(reviewService.deleteReview(userDetails, reviewId));
    }

    //api 짐 리뷰 상세 조회
    @GetMapping("/reviews/{reviewId}")
    public ResponseDto<ReviewResponseDto> getReview(@PathVariable Long reviewId) {
        return ResponseDto.success(reviewService.getReview(reviewId));
    }


}
