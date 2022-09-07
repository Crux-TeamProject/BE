package com.project.crux.domain.response;

import com.project.crux.domain.Review;
import com.project.crux.domain.ReviewPhoto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {

    private Long id;
    private int score;
    private String content;
    private List<ReviewPhotoResponseDto> reviewPhotoList = new ArrayList<>();

    public ReviewResponseDto(Review review) {
        this.id = review.getId();
        this.score = review.getScore();
        this.content = review.getContent();
    }
}
