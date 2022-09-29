package com.project.crux.crew.domain;


import com.project.crux.common.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Notice extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String date;

    @Column
    private String place;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private CrewMember crewMember;

    public Notice(String date, String place, String content) {
        this.date = date;
        this.place = place;
        this.content = content;
    }

    public void update(String date, String place, String content) {
        this.date = date;
        this.place = place;
        this.content = content;
    }
}
