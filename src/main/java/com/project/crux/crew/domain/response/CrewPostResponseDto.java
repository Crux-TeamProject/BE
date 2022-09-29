package com.project.crux.crew.domain.response;

import com.project.crux.crew.domain.CrewPost;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CrewPostResponseDto {
    private Long postId;
    private List<CrewPhotoResponseDto> imgList;
    private Long crewId;
    private Long authorId;
    private LocalDateTime createdAt;

    public CrewPostResponseDto(CrewPost crewPost) {
        this.postId = crewPost.getId();
        this.imgList = crewPost.getPhotoList().stream().map(CrewPhotoResponseDto::new).collect(Collectors.toList());
        this.crewId = crewPost.getCrewMember().getCrew().getId();
        this.authorId = crewPost.getCrewMember().getMember().getId();
        this.createdAt = crewPost.getCreatedAt();
    }
}
