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
    private long userId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "user_nickname")
    private String userNickname;

    @CreationTimestamp
    @Column(name = "reg_date", nullable = false)
    private OffsetDateTime regDate;

    @Column(name = "bookshelf_name", nullable = false)
    private String bookshelfName;

    @Builder
    public User(String email, String password, String userNickname, String bookshelfName) {
        this.email = email;
        this.password = password;
        this.userNickname = userNickname;
        this.bookshelfName = bookshelfName;
    }


}
