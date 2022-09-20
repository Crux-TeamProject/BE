package com.project.crux.gym.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ReviewPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String imgUrl;

    @JoinColumn(name = "review_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;
}
