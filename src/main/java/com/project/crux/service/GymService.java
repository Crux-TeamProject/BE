package com.project.crux.service;

import com.project.crux.domain.*;
import com.project.crux.domain.response.GymResponseDto;
import com.project.crux.domain.response.ReviewPhotoResponseDto;
import com.project.crux.domain.response.ReviewResponseDto;
import com.project.crux.exception.CustomException;
import com.project.crux.exception.ErrorCode;
import com.project.crux.repository.GymRepository;
import com.project.crux.repository.LikeGymRepository;
import com.project.crux.repository.ReviewPhotoRepository;
import com.project.crux.repository.ReviewRepository;
import com.project.crux.security.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GymService {

    private final GymRepository gymRepository;
    private final LikeGymRepository likeGymRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewPhotoRepository reviewPhotoRepository;


    public List<GymResponseDto> getGyms(String lat, String lon, Pageable pageable) {

        Page<Gym> gyms = gymRepository.findByLatAndLon(lat, lon, pageable);

        return pageToDtoListWithDist(gyms,lon,lat);
    }


/*    public List<GymResponseDto> getPopularGyms(double lastAvgScore, int size) {

        if (lastAvgScore < 0 || 5 < lastAvgScore) {
            throw new CustomException(ErrorCode.INVALID_AVGSCORE);
        }
        PageRequest pageRequest = PageRequest.of(0, size, Sort.by("avgScore").descending());

        Page<Gym> gyms = gymRepository.findByAvgScoreLessThan(lastAvgScore, pageRequest);

        return pageToDtoList(gyms);
    }*/

    public List<GymResponseDto> getPopularGyms(Pageable pageable) {

        Page<Gym> gyms = gymRepository.findAll(pageable);

        return pageToDtoList(gyms);
    }


/*    public List<GymResponseDto> getSearchGyms(String query, Long lastArticleId, int size) {

        if (lastArticleId < 0 || Integer.MAX_VALUE < lastArticleId) {
            throw new CustomException(ErrorCode.INVALID_ARTICLEID);

        }
        PageRequest pageRequest = PageRequest.of(0, size, Sort.by("id").descending());

        Page<Gym> gyms = gymRepository.findByIdLessThanAndNameContains(lastArticleId, query, pageRequest);

        return pageToDtoList(gyms);
    }*/

    public List<GymResponseDto> getSearchGyms(String query, Pageable pageable) {

        Page<Gym> gyms = gymRepository.findByNameContains(query, pageable);

        return pageToDtoList(gyms);
    }


    public GymResponseDto getGym(Long gymId) {

        Gym gym = getGymById(gymId);

        GymResponseDto gymResponseDto = new GymResponseDto(gym);

        List<Review> reviewList = reviewRepository.findByGym(gym);

        List<ReviewResponseDto> reviews = gymResponseDto.getReviews();

        getReviewResponseList(reviewList, reviews);

        return gymResponseDto;
    }

    @Transactional
    public String likeGym(UserDetailsImpl userDetails, Long gymId) {

        Member member = userDetails.getMember();
        Gym gym = getGymById(gymId);

        LikeGym existLike = likeGymRepository.findByMemberAndGymId(member, gymId);

        if (existLike == null) {
            LikeGym likeGym = new LikeGym(member,gym);
            likeGymRepository.save(likeGym);

            return "즐겨 찾기 추가 완료";
        }
        likeGymRepository.delete(existLike);

        return "즐겨 찾기 삭제 완료";
    }

   private void getReviewResponseList(List<Review> reviewList, List<ReviewResponseDto> reviews) {
        reviewList.forEach(review -> {
            ReviewResponseDto reviewResponseDto = new ReviewResponseDto(review);
            List<ReviewPhoto> reviewPhotoList = reviewPhotoRepository.findAllByReview(review);
            reviewPhotoList.forEach(reviewPhoto -> reviewResponseDto.getReviewPhotoList().add(new ReviewPhotoResponseDto(reviewPhoto)));
            reviews.add(reviewResponseDto);
        });
    }

    private List<GymResponseDto> pageToDtoList(Page<Gym> gyms) {

        List<GymResponseDto> gymResponseDtos = new ArrayList<>();

        gyms.getContent().forEach(gym -> gymResponseDtos.add(new GymResponseDto(gym)));

        return gymResponseDtos;
    }

    private Gym getGymById(Long gymId) {
        return gymRepository.findById(gymId).orElseThrow(() -> new CustomException(ErrorCode.GYM_NOT_FOUND));
    }

    private List<GymResponseDto> pageToDtoListWithDist(Page<Gym> gyms, String lon, String lat){

        List<GymResponseDto> gymResponseDtos = new ArrayList<>();

        gyms.getContent().forEach(gym ->
            gymResponseDtos.add(new GymResponseDto(gym,distance(Double.parseDouble(lat),Double.parseDouble(lon),
                    Double.parseDouble(gym.getLat()),Double.parseDouble(gym.getLon()))))
        );

        return gymResponseDtos;

    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        dist = Math.round(dist * 100) / (double) 100 ;

        return (dist);
    }

    // This function converts decimal degrees to radians
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

}
