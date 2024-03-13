package com.cos.photogramstart.domain.user;

import com.cos.photogramstart.domain.image.Image;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

// JPA - Java Persistence API(자바로 데이터를 영구적으로 저장(DB)할 수 있는 API를 제공)
@Builder    // Builder 패턴으로 데이터를 담을 수 있게 해주는 어노테이션
@AllArgsConstructor // 모든 생성자를 자동으로 만들어주는 어노테이션
@NoArgsConstructor // 빈 생성자를 자동으로 만들어주는 어노테이션
@Data   // 자동으로 Getter, Setter, toString을 만들어주는 어노테이션
@Entity // DB에 테이블을 생성해주는 어노테이션
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String username; // 아이디
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;
    private String website; // 웹 사이트
    private String bio; // 자기소개
    @Column(nullable = false)
    private String email;
    private String phone;
    private String gender;

    private String profileImageUrl; // 사진
    private String role; // 권한

    // 나는 연관관계의 주인이 아니다. 그러므로 테이블에 컬럼을 만들지마.
    // User를 select 할 때 해당 user id로 등록된 image들을 다 가져와.
    // Lazy = user를 select 할 때 해당 user id로 등록된 image들을 가져오지마 - 대신 getImages()함수의 image들이 호출될 때 가져와
    // Eager = user를 select 할 때 해당 userid로 등록된 image들을 전부 join해서 가져와.
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"user"})
    private List<Image> images; // 양방향 매핑

    private LocalDateTime createDate;

    @PrePersist // 디비에 INSERT 되기 직전에 실행
    public void createDate() {
        this.createDate = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "User [bio=" + bio + ", createDate=" + createDate + ", email=" + email + ", gender=" + gender + ", id="
                + id + ", name=" + name + ", password=" + password + ", phone=" + phone + ", profileImageUrl="
                + profileImageUrl + ", role=" + role + ", username=" + username + ", website=" + website + "]";
    }

}
