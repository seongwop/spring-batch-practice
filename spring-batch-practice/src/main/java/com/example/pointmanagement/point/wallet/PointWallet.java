package com.example.pointmanagement.point.wallet;

import com.example.pointmanagement.point.IdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigInteger;

@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class PointWallet extends IdEntity {
    @Column(name = "user_id", unique = true, nullable = false)
    String userId;
    @Setter
    @Column(name = "amount", columnDefinition = "BIGINT")
    BigInteger amount;
}
