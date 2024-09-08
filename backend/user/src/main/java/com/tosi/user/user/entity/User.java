package com.tosi.user.user.entity;

import com.ssafy.tosi.user.dto.UserInfo;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable=false)
    private Integer userId;

    @Column(name = "email", nullable = false, unique = true, updatable = false)
    private String email;

    private String password;

    @CreatedDate
    @Column(name = "regDate", nullable = false)
    private LocalDateTime regDate;

    @Column(name = "bookshelfName", nullable = false)
    private String bookshelfName = "나의 책장";

    @OneToMany
    @JoinColumn(name = "userId")
    @OrderBy("myBaby desc, childName asc")
    private List<Child> childrenList = new ArrayList<>();

    @Builder
    public User (String email, String password, String bookshelfName) {
        this.email = email;
        this.password = password;
        this.bookshelfName = bookshelfName;
    }

    public void update(UserInfo userInfo) {
        this.password = userInfo.getPassword();
        this.bookshelfName = userInfo.getBookshelfName();
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", regDate=" + regDate +
                ", bookshelfName='" + bookshelfName + '\'' +
                ", childrenList=" + childrenList +
                '}';
    }

}
