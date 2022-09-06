package com.project.crux.domain.request;

import com.project.crux.domain.ReviewPhoto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {

    private int score;
    private String content;
    private List<ReviewPhoto> reviewPhotoList;

}
