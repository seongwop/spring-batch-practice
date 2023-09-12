package com.example.pointmanagement.point.reservation;

import java.time.LocalDate;

public interface PointReservationCustomRepository {

    Long findMinId(LocalDate today);

    Long findMaxId(LocalDate today);
}
