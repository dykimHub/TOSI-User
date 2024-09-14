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
    private long childId;
    @Column(name = "user_id", nullable = false)
    private long userId;
    @Column(name = "child_name", nullable = false)
    private String childName;
    @Column(name = "gender", nullable = false)
    private int gender;

    @Builder
    public Child(String childName, int gender) {
        this.childName = childName;
        this.gender = gender;
    }
}