package com.cos.photogramstart.domain.likes;

import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder // Builder 패턴으로 데이터를 담을 수 있게 해주는 어노테이션
@AllArgsConstructor // 모든 생성자를 자동으로 만들어주는 어노테이션
@NoArgsConstructor // 빈 생성자를 자동으로 만들어주는 어노테이션
@Data // 자동으로 Getter, Setter, toString을 만들어주는 어노테이션
@Entity // DB에 테이블을 생성해주는 어노테이션
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name="likes_uk", // Unique 제약조건 이름
                        columnNames = { // Unique 제약조건을 적용할 컬럼명
                                "imageId",
                                "userId"
                        }
                )
        }
)
public class Likes {

    @Id // Primary Key를 지정해주는 어노테이션
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    //무한참조됨
    @ManyToOne
    @JoinColumn(name = "imageId")
    private Image image;

    @JsonIgnoreProperties({"images"})
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    private LocalDateTime createDate; // 데이터가 입력된 시간.

    @PrePersist
    public void createDate() {
        this.createDate = LocalDateTime.now();
    }
}
