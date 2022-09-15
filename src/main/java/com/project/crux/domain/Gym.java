package com.project.crux.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Gym {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(length = 500)
    private String imgUrl;

    @Column
    private String phone;

    @Column
    private String lon;

    @Column
    private String lat;

    @Column
    private double avgScore;

    @OneToMany(mappedBy = "gym", cascade = CascadeType.REMOVE)
    private List<Review> reviewList = new ArrayList<>();

    public Gym(String place_name, String address_name, String phone, String lon , String lat) {
        this.name = place_name;
        this.location = address_name;
        this.phone = phone;
        this.lon = lon;
        this.lat = lat;
    }

    public Gym(String place_name, String address_name, String phone, double avgScore) {
        this.name = place_name;
        this.location = address_name;
        this.phone = phone;
        this.avgScore = avgScore;
    }

    public void update(String place_name, String address_name, String phone, String lon, String lat) {
        this.name = place_name;
        this.location = address_name;
        this.phone = phone;
        this.lon = lon;
        this.lat = lat;
    }

    public void updateImg(String image_url) {
        this.imgUrl = image_url;
    }


    public void updateScore(int score) {
        this.avgScore = (avgScore * reviewList.size() + score) / (reviewList.size() + 1);
    }
}
