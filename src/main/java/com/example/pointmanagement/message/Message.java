package com.example.pointmanagement.message;

import com.example.pointmanagement.point.IdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Message extends IdEntity {
    @Column(name = "user_id", nullable = false)
    String userId;
    @Column(name = "title", nullable = false)
    String title;
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    String content;
}
