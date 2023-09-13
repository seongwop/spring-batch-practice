package com.example.pointmanagement.point.reservation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface PointReservationCustomRepository {
    Page<PointReservation> findPointReservationToExecute(LocalDate today, Long minId, Long maxId, Pageable pageable);

    Long findMinId(LocalDate today);
    Long findMaxId(LocalDate today);
}
