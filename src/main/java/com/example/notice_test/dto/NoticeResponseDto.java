package com.example.notice_test.dto;

import com.example.notice_test.entity.Comment;
import com.example.notice_test.entity.Notice;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// DTO 반환이라는 과제 조건이 있음.
//

@Getter
public class NoticeResponseDto {
    private Long id;
    private String username;
    private String title;
    private String contents;
//    private String password; // 비밀번호 표시 안되게 변경
    private LocalDateTime createdat;
    private LocalDateTime modifiedat;
    private List<Comment> comment;

//    public NoticeResponseDto(Notice notice) {
//        this.id = notice.getId();
//        this.username = notice.getUser().getUsername();
//        this.title = notice.getTitle();
//        this.contents = notice.getContents();
//        this.createdat = notice.getCreatedAt();
//        this.modifiedat = notice.getModifiedAt();
//    }

    public NoticeResponseDto(Notice notice, List<Comment> comment) {
        this.id = notice.getId();
        this.username = notice.getUser().getUsername();
        this.title = notice.getTitle();
        this.contents = notice.getContents();
        this.createdat = notice.getCreatedAt();
        this.modifiedat = notice.getModifiedAt();
        this.comment = comment;
    }

}


//        this.password = notice.getPassword();