package com.project.crux.crew.domain.request;

import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrewRequestDto {
    private String name;
    private String content;
    private String imgUrl;
    private String mainActivityGym;
    private String mainActivityArea;
    private List<String> keywords;
}
