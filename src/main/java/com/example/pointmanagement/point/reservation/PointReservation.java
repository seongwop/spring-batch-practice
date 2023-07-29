package com.example.pointmanagement.point.reservation;

import com.example.pointmanagement.point.IdEntity;
import com.example.pointmanagement.point.wallet.PointWallet;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDate;

@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PointReservation extends IdEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "point_wallet_id", nullable = false)
    PointWallet pointWallet;

    @Column(name = "amount", nullable = false, columnDefinition = "BIGINT")
    BigInteger amount;

    @Column(name = "earned_date", nullable = false)
    LocalDate earnedDate;

    @Column(name = "available_days", nullable = false)
    int availableDays;

    @Column(name = "is_executed", columnDefinition = "TINYINT", length = 1)
    boolean executed;

    public PointReservation (
            PointWallet pointWallet,
            BigInteger amount,
            LocalDate earnedDate,
            int availableDays
    ) {
        this.pointWallet = pointWallet;
        this.amount = amount;
        this.earnedDate = earnedDate;
        this.availableDays = availableDays;
        this.executed = false;
    }

    public void execute() {
        this.executed = true;
    }

    public LocalDate getExpiredDate() {
        return this.earnedDate.plusDays(this.availableDays);
    }
}
