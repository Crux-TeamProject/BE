package com.project.crux.gym.repository;

import com.project.crux.gym.domain.Gym;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.project.crux.gym.domain.QGym.*;

@Repository
@RequiredArgsConstructor
public class GymRepositoryImpl implements GymRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Gym> findByCustomCursor(double customCursor, PageRequest pageRequest) {

        List<Gym> gymList = queryFactory.selectFrom(gym)
                                    .where(customCursor(customCursor))
                                    .orderBy(gym.avgScore.add(gym.id.divide(100000.0)).desc())
                                    .limit(pageRequest.getPageSize())
                                    .fetch();

        return new PageImpl<>(gymList, pageRequest, pageRequest.getPageSize());
    }

    private BooleanExpression customCursor(double customCursor) {
        return gym.avgScore.add(gym.id.divide(100000.0)).lt(customCursor);
        }

}
