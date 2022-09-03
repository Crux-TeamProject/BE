package com.project.crux.infrastructure.kakao.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Document {
    private String place_name;
    private String address_name;
    private String phone;
    private String place_url;
    private String x;
    private String y;
    private String image_url;
}