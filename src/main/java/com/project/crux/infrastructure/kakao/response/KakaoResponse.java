package com.project.crux.infrastructure.kakao.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class KakaoResponse {
    private List<Document> documents;
    private Meta meta;
}
