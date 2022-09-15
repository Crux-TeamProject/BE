package com.project.crux.infrastructure.kakao.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Getter
@NoArgsConstructor
public class Meta {
    @Column(unique = true)
    private int total_count;
}