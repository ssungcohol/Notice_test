package com.example.notice_test.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NoticeRequestDto {  //클라이언트로부터 받아올 것들을 객체를 통해서 받아옴

//    private String username;  //작성자명
    private String contents;  //내용
    private String title;  //제목
//    private String password;  //비밀번호
}