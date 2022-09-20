package com.project.crux.gym.domain.request;

import com.project.crux.gym.domain.ReviewPhoto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {

    @Min(value = 0, message = "0 이상만 가능합니다")
    @Max(value = 5, message = "5 이하만 가능합니다")
    private int score;
    private String content;
    private List<ReviewPhoto> reviewPhotoList;

}
