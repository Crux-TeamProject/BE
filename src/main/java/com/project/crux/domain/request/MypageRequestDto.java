package com.project.crux.domain.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MypageRequestDto {
    private String content;
    private String nickname;
    private String imgUrl;
}
