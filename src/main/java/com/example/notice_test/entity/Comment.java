package com.example.notice_test.entity;

import com.example.notice_test.dto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Getter
@Entity
@NoArgsConstructor
public class Comment extends Timestamped {

    // 댓글 id PK
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    // 댓글 내용
    @Column(nullable = false)
    private String comment;

    // 회원 id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    // 게시물 id
    @ManyToOne
    @JoinColumn(name = "notice_id")
    private Notice notice;

    // 댓글 작성 시
    public Comment(CommentRequestDto requestDto, User user, Notice notice) {
        this.comment = requestDto.getComment(); //Getter 필요?
        this.user = user;
        this.notice = notice;
    }

    // 댓글 수정 시
    public void update(CommentRequestDto requestDto) {
        this.comment = requestDto.getComment();
    }
}
