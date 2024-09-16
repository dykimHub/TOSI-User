package com.tosi.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;


@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", nullable = false, unique = true)
    private String nickname;

    @CreationTimestamp
    @Column(name = "reg_date", nullable = false)
    private OffsetDateTime regDate;

    @Column(name = "bookshelf_name", nullable = false)
    private String bookshelfName;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Builder
    public User(String email, String password, String nickname, String bookshelfName, UserRole role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.bookshelfName = bookshelfName;
        this.role = role;
    }

}
