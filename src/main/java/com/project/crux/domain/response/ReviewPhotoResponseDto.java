package com.project.crux.domain.response;

import com.project.crux.domain.ReviewPhoto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewPhotoResponseDto {

    private String imgUrl;

    public ReviewPhotoResponseDto(ReviewPhoto reviewPhoto) {
        this.imgUrl = reviewPhoto.getImgUrl();
    }
}
