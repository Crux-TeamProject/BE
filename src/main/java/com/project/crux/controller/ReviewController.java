package com.project.crux.controller;

import com.project.crux.domain.request.ReviewRequestDto;
import com.project.crux.domain.response.ResponseDto;
import com.project.crux.domain.response.ReviewResponseDto;
import com.project.crux.security.jwt.UserDetailsImpl;
import com.project.crux.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    //api 짐 리뷰 작성
    @PostMapping("/reviews/{gymId}")
    public ResponseDto<ReviewResponseDto> createReview(@RequestBody ReviewRequestDto requestDto, @PathVariable Long gymId,
                                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(reviewService.createReview(requestDto, gymId, userDetails));
    }

    //api 짐 리뷰 수정
    @PutMapping("/reviews/{gymId}/{reviewId}")
    public ResponseDto<ReviewResponseDto> updateReview(@RequestBody ReviewRequestDto requestDto,
                                                       @PathVariable Long gymId, @PathVariable Long reviewId,
                                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(reviewService.updateReview(requestDto ,gymId, reviewId ,userDetails));
    }

    //api 짐 리뷰 상세 조회
    @GetMapping("/reviews/{reviewId}")
    public ResponseDto<ReviewResponseDto> getReview(@PathVariable Long reviewId) {
        return ResponseDto.success(reviewService.getReview(reviewId));
    }

    //api 짐 리뷰 전체 조회
    @GetMapping("/reviews/{gymId}")
    public ResponseDto<List<ReviewResponseDto>> getAllReviews(@PathVariable Long gymId) {
        return ResponseDto.success(reviewService.getAllReviews(gymId));
    }

}
