package com.project.crux.crew.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Crew {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String content;

    @Column
    private String imgUrl;

    @Column
    private String mainActivityGym;

    @Column
    private String mainActivityArea;

    @Column
    private String keywords;

    @OneToMany(mappedBy = "crew", cascade = CascadeType.REMOVE)
    private List<LikeCrew> likes;

    @Formula("(select count(*) from like_crew lc where lc.crew_id = id)")
    private int countOfLike;

    @OneToMany(mappedBy = "crew", cascade = CascadeType.REMOVE)
    private List<CrewMember> crewMemberList = new ArrayList<>();

    @OneToOne(mappedBy = "crew", cascade = CascadeType.REMOVE)
    private ChatRoom chatRoom;

    @Builder
    public Crew(String name, String content, String imgUrl, String mainActivityGym, String mainActivityArea, String keywords) {
        this.name = name;
        this.content = content;
        this.imgUrl = imgUrl;
        this.mainActivityGym = mainActivityGym;
        this.mainActivityArea = mainActivityArea;
        this.keywords = keywords;
    }

    public void update(String name, String content, String imgUrl, String mainActivityGym, String mainActivityArea, String keywords) {
        this.name = name;
        this.content = content;
        this.imgUrl = imgUrl;
        this.mainActivityGym = mainActivityGym;
        this.mainActivityArea = mainActivityArea;
        this.keywords = keywords;
    }
}
