package com.project.crux.crew.repository;

import com.project.crux.crew.domain.Crew;
import com.project.crux.crew.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice,Long> {
    List<Notice> findAllByCrewMember_Crew(Crew crew);
}
