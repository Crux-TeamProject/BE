package com.project.crux.sse.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class NotificationContent {

    @Column(nullable = false)
    private Long crewId;

    @Column(nullable = false)
    private String content;


}
