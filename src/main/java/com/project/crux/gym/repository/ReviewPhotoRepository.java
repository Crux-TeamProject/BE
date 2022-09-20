package com.project.crux.gym.repository;


import com.project.crux.gym.domain.Review;
import com.project.crux.gym.domain.ReviewPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewPhotoRepository extends JpaRepository<ReviewPhoto, Long> {
    List<ReviewPhoto> findAllByReview(Review review);
}
