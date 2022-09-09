package com.project.crux.domain.response;

import com.project.crux.domain.CrewPhoto;
import com.project.crux.domain.CrewPost;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CrewPostResponseDto {
    List<String> imgList;

    public CrewPostResponseDto(CrewPost crewPost) {
        this.imgList = crewPost.getPhotoList().stream().map(CrewPhoto::getImgUrl).collect(Collectors.toList());
    }
}
