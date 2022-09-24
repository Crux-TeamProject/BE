package com.project.crux.gym.domain.response;

import com.project.crux.gym.domain.Gym;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class GymResponseDto {

    private Long id;
    private String name;
    private String location;
    private String imgUrl;
    private String phone;
    private String lon;
    private String lat;
    private double avgScore;
    private double dist;
    private boolean likeGym;
    private Long likeNum;
    private List<ReviewResponseDto> reviews;


    public static GymResponseDto of(Gym gym, double dist) {
        return GymResponseDto.builder()
                .id(gym.getId())
                .name(gym.getName())
                .location(gym.getLocation())
                .imgUrl(gym.getImgUrl())
                .phone(gym.getPhone())
                .lon(gym.getLon())
                .lat(gym.getLat())
                .avgScore(gym.getAvgScore())
                .dist(dist)
                .reviews(new ArrayList<>())
                .build();
    }

    public static GymResponseDto from(Gym gym) {
        return GymResponseDto.builder()
                .id(gym.getId())
                .name(gym.getName())
                .location(gym.getLocation())
                .imgUrl(gym.getImgUrl())
                .phone(gym.getPhone())
                .lon(gym.getLon())
                .lat(gym.getLat())
                .likeGym(false)
                .avgScore(gym.getAvgScore())
                .reviews(new ArrayList<>())
                .build();
    }

}
