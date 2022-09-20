package com.project.crux.crew.domain.request;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrewRequestDto {
    private String name;
    private String content;
    private String imgUrl;
}
