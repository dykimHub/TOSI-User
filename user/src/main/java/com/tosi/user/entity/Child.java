package com.tosi.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "children")
@Entity
public class Child {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "child_id", nullable = false)
    private Long childId;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "child_name", nullable = false)
    private String childName;
    @Column(name = "child_gender", nullable = false)
    private int childGender;

    @Builder
    public Child(Long userId, String childName, int childGender) {
        this.userId = userId;
        this.childName = childName;
        this.childGender = childGender;
    }
}