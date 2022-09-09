package com.project.crux.domain.response;

import com.project.crux.domain.Gym;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GymResponseDto {

    private Long id;
    private String name;
    private String location;
    private String imgUrl;
    private String phone;
    private double avgScore;
    private List<ReviewResponseDto> reviews = new ArrayList<>();

    public GymResponseDto(Gym gym) {
        this.id = gym.getId();
        this.name = gym.getName();
        this.location = gym.getLocation();
        this.imgUrl = gym.getImgUrl();
        this.phone = gym.getPhone();
        this.avgScore = gym.getAvgScore();
    }

}
