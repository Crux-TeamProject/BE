package com.project.crux.crew.domain.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CrewPhotoRequestDto {
    private List<String> imgList;
}
