package com.project.crux.gym.repository;

import com.project.crux.gym.domain.Gym;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GymRepository extends JpaRepository<Gym, Long>,GymRepositoryCustom {
    Optional<Gym> findByName(String place_name);
    Page<Gym> findByIdLessThanAndNameContains(Long lastArticleId ,String name, PageRequest pageRequest);
    Page<Gym> findByNameContains(String query, Pageable pageable);


    @Query
            (
                    value = "SELECT * , ST_Distance_Sphere(Point(:lon,:lat),Point(g.lon,g.lat)) as dist" +
                            " FROM gym as g order by dist ASC ",
                    countQuery = "SELECT * FROM gym", nativeQuery = true
            )
    Page<Gym> findByLatAndLon(@Param("lat") String lat, @Param("lon") String lon, Pageable pageable);
}
