package com.project.crux.gym.domain.response;


import com.project.crux.member.domain.Member;
import com.project.crux.gym.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {

    private Long memberId;
    private String nickname;
    private String imgUrl;
    private Long id;
    private int score;
    private String content;
    private LocalDateTime createdAt;
    private List<ReviewPhotoResponseDto> reviewPhotoList = new ArrayList<>();

    public ReviewResponseDto(Member member, Review review) {
        this.memberId = member.getId();
        this.nickname = member.getNickname();
        this.imgUrl = member.getImgUrl();
        this.id = review.getId();
        this.score = review.getScore();
        this.content = review.getContent();
        this.createdAt = review.getCreatedAt();
    }

    public ReviewResponseDto(Review review) {
        this.id = review.getId();
        this.score = review.getScore();
        this.content = review.getContent();
        this.createdAt = review.getCreatedAt();
    }
}
