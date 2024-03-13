package com.cos.photogramstart.domain.image;

import com.cos.photogramstart.domain.comment.Comment;
import com.cos.photogramstart.domain.likes.Likes;
import com.cos.photogramstart.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Image { // N, 1
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String caption; // 오늘 나 너무 피곤해!!
    private String postImageUrl; // 경로

    @JsonIgnoreProperties({"images"})
    @JoinColumn(name = "userId")
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    // 이미지 좋아요
    @JsonIgnoreProperties({"image"})
    @OneToMany(mappedBy = "image")
    private List<Likes> likes;

    // 이미지 좋아요 여부 상태
    @Transient // DB에 해당 컬럼을 생성하지 않게 만드는 어노테이션
    private boolean likeState;

    @Transient
    private int likeCount;

    // 댓글 정보
    @OrderBy("id DESC")
    @JsonIgnoreProperties({"image"})
    @OneToMany(mappedBy = "image")
    private List<Comment> comments;

    private LocalDateTime createDate;

    @PrePersist // 디비에 INSERT 되기 직전에 실행
    public void createDate(){
        this.createDate = LocalDateTime.now();
    }

    // 오브젝트를 콘솔에 출력할떄,  무한참조가 발생하는 User오브젝트를 삭제한 toString
    // @Override
    // public String toString() {
    //     return "Image [caption=" + caption + ", createDate=" + createDate + ", id=" + id + ", postImageUrl="
    //             + postImageUrl + "]";
    // }

}
