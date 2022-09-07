package com.project.crux.domain;

import com.project.crux.common.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;


import javax.persistence.*;
@Getter
@NoArgsConstructor
@Entity
public class MemberCrew {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "crew_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Crew crew;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Status status = Status.SUBMIT;

    public MemberCrew(Member member, Crew crew) {
        this.member = member;
        this.crew = crew;
        member.getMemberCrewList().add(this);
        crew.getMemberCrewList().add(this);
    }

    public void updateStatus(Status status) {
        this.status = status;
    }
}
