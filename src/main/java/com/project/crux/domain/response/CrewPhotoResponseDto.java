package com.project.crux.domain.response;

import com.project.crux.domain.CrewPhoto;
import lombok.Getter;

@Getter
public class CrewPhotoResponseDto {
    private Long photoId;
    private String imgUrl;

    public CrewPhotoResponseDto(CrewPhoto photo) {
        this.photoId = photo.getId();
        this.imgUrl = photo.getImgUrl();
    }
}
