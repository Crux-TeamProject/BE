package com.project.crux.domain.response;

import com.project.crux.domain.CrewPost;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CrewPostResponseDto {
    Long postId;
    List<CrewPhotoResponseDto> imgList;

    public CrewPostResponseDto(CrewPost crewPost) {
        this.postId = crewPost.getId();
        this.imgList = crewPost.getPhotoList().stream().map(CrewPhotoResponseDto::new).collect(Collectors.toList());
    }
}
