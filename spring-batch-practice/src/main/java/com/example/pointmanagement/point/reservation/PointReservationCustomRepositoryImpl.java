package com.example.pointmanagement.point.reservation;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDate;

public class PointReservationCustomRepositoryImpl extends QuerydslRepositorySupport implements PointReservationCustomRepository {
    public PointReservationCustomRepositoryImpl() {
        super(PointReservation.class);
    }
    @Override
    public Long findMinId(LocalDate today) {
        QPointReservation pointReservation = QPointReservation.pointReservation;
        return from(pointReservation)
                .select(pointReservation.id.min())
                .where(pointReservation.earnedDate.loe(today))
                .where(pointReservation.executed.eq(false))
                .fetchOne();
    }

    @Override
    public Long findMaxId(LocalDate today) {
        QPointReservation pointReservation = QPointReservation.pointReservation;
        return from(pointReservation)
                .select(pointReservation.id.max())
                .where(pointReservation.earnedDate.loe(today))
                .where(pointReservation.executed.eq(false))
                .fetchOne();
    }
}
