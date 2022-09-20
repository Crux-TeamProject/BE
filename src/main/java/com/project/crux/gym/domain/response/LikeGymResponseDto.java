package com.project.crux.gym.domain.response;

import com.project.crux.gym.domain.Gym;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LikeGymResponseDto {
    private Long gymId;
    private double avgScore;
    private String name;
    private String imgUrl;


    public LikeGymResponseDto(Gym gym) {
        this.gymId = gym.getId();
        this.avgScore = gym.getAvgScore();
        this.name = gym.getName();
        this.imgUrl = gym.getImgUrl();
    }
}
