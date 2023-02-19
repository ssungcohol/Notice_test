package com.example.notice_test.dto;

import com.example.notice_test.entity.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {

    // 클라한테 뭐 보여주면 될까?

    private Long id;

    private String username;
    // 작성자

    private String comment;
    // 댓글 내용


    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.username = comment.getUser().getUsername();
        this.comment = comment.getComment();
    }
}
