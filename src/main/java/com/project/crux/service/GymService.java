package com.project.crux.service;

import com.project.crux.domain.Gym;
import com.project.crux.domain.response.GymResponseDto;
import com.project.crux.exception.CustomException;
import com.project.crux.exception.ErrorCode;
import com.project.crux.repository.GymRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GymService {

    private final GymRepository gymRepository;

    @Transactional
    public List<GymResponseDto> getPopularGyms(double lastAvgScore, int size) {

        if (lastAvgScore < 0 || 5 < lastAvgScore) {
            throw new CustomException(ErrorCode.INVALID_AVGSCORE);
        }
        PageRequest pageRequest = PageRequest.of(0, size, Sort.by("avgScore").descending());

        Page<Gym> gyms = gymRepository.findByAvgScoreLessThan(lastAvgScore, pageRequest);

        return pageToDtoList(gyms);
    }

    public List<GymResponseDto> getSearchGyms(String query, Long lastArticleId, int size) {

        if (lastArticleId < 0 || Integer.MAX_VALUE < lastArticleId) {
            throw new CustomException(ErrorCode.INVALID_ARTICLEID);

        }
        PageRequest pageRequest = PageRequest.of(0, size, Sort.by("id").descending());

        Page<Gym> gyms = gymRepository.findByIdLessThanAndNameContains(lastArticleId, query, pageRequest);

        return pageToDtoList(gyms);
    }


    public GymResponseDto getGym(Long gymId) {

        Gym gym = gymRepository.findById(gymId).orElseThrow(() -> new CustomException(ErrorCode.GYM_NOT_FOUND));

        return new GymResponseDto(gym);
    }


    private List<GymResponseDto> pageToDtoList(Page<Gym> gyms) {

        List<GymResponseDto> gymResponseDtos = new ArrayList<>();

        gyms.getContent().forEach(gym -> gymResponseDtos.add(new GymResponseDto(gym)));

        return gymResponseDtos;
    }
}
