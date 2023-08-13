package com.example.pointmanagement.message;

import com.example.pointmanagement.point.IdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Message extends IdEntity {
    @Column(name = "user_id", nullable = false)
    String userId;
    @Column(name = "title", nullable = false)
    String title;
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    String content;

    public static Message expiredPointMessageInstance(
            String userId, LocalDate expiredDate, BigInteger expiredAmount
    ) {
        return new Message(
                userId,
                String.format("%s 포인트 만료", expiredAmount.toString()),
                String.format("%s 기준 %s 포인트가 만료되었습니다.", expiredDate.format(DateTimeFormatter.ISO_DATE), expiredAmount)
        );
    }
}
