package com.project.crux.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    private double avgScore;

    public Gym(String place_name, String address_name, String phone) {
        this.name = place_name;
        this.location = address_name;
        this.phone = phone;
    }

    public void update(String place_name, String address_name, String phone) {
        this.name = place_name;
        this.location = address_name;
        this.phone = phone;
    }

    public void updateImg(String image_url) {
        this.imgUrl = image_url;
    }
}
