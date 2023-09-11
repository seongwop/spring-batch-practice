package com.example.pointmanagement.point;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface PointCustomRepository {
    Page<ExpiredPointSummary> sumByExpiredDate(LocalDate alarmCriteriaDate, Pageable pageable);
    Page<ExpiredPointSummary> sumBeforeCriteriaDate(LocalDate alarmCriteriaDate, Pageable pageable);

    // select p from Point p where p.expireDate < :today and used = false and expired = false
    Page<Point> findPointToExpire(LocalDate today, Pageable pageable);
}

