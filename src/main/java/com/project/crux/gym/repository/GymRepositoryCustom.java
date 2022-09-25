package com.project.crux.gym.repository;

import com.project.crux.gym.domain.Gym;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface GymRepositoryCustom {

    Page<Gym> findByCustomCursor(double customCursor, PageRequest pageRequest);
}
