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

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private CrewMember crewMember;

    public Notice(String content) {
        this.content = content;
    }

    public void update(String content) {
        this.content = content;
    }
}
