package com.project.crux.domain.response;

import com.project.crux.domain.CrewPost;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CrewPostResponseDto {
    private Long postId;
    private List<CrewPhotoResponseDto> imgList;
    private LocalDateTime createdAt;

    public CrewPostResponseDto(CrewPost crewPost) {
        this.postId = crewPost.getId();
        this.imgList = crewPost.getPhotoList().stream().map(CrewPhotoResponseDto::new).collect(Collectors.toList());
        this.createdAt = crewPost.getCreatedAt();
    }
}
