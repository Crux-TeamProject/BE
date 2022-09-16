package com.project.crux.repository;


import com.project.crux.domain.Gym;
import com.project.crux.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByGym(Gym gym);
    List<Review> findByGymOrderByIdDesc(Gym gym);

}
