package com.project.crux.gym.domain;

import com.project.crux.common.Timestamped;
import com.project.crux.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Review extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int score;

    @Column(nullable = false)
    private String content;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "gym_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Gym gym;

    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE)
    public List<ReviewPhoto> reviewPhotoList;

    public Review(int score, String content, Member member) {
        this.score = score;
        this.content = content;
        this.member = member;
    }

    public void update(int score, String content) {
        this.score = score;
        this.content = content;
    }
}
