package com.example.notice_test.controller;

import com.example.notice_test.dto.CommentMessageDto;
import com.example.notice_test.dto.CommentRequestDto;
import com.example.notice_test.dto.CommentResponseDto;
import com.example.notice_test.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequiredArgsConstructor // DI 중의 하나로 알아서 생성자 생성해줌
public class CommentController {

    private final CommentService commentService;

    // 댓글 조회
//    @GetMapping ("/api/notice/{noticeId}/comments")
//    public List<CommentResponseDto> getComment(@PathVariable Long noticeId) {
//        return commentService.getComment(noticeId);
//    }


    // 댓글 작성
    @PostMapping("/api/notice/{noticeId}/comment")
    public CommentResponseDto creatComment(@PathVariable Long noticeId, @RequestBody CommentRequestDto comRequestDto, HttpServletRequest request) {

        return commentService.creatComment(noticeId, comRequestDto, request);
    }

    // 댓글 수정
    @PutMapping("api/notice/{noticeId}/comment/{commentId}")
    public CommentResponseDto updateComment(@PathVariable Long noticeId, @PathVariable Long commentId, @RequestBody CommentRequestDto comRequestDto, HttpServletRequest request) {

        return commentService.updateComment(noticeId, commentId, comRequestDto, request);
    }


    // 댓글 수정
    @DeleteMapping("/api/notice/{noticeId}/comment/{commentId}")
    public CommentMessageDto deleteComment(@PathVariable Long noticeId, @PathVariable Long commentId, @RequestBody CommentRequestDto comRequestDto, HttpServletRequest request) {

        return commentService.deleteComment(noticeId, commentId, comRequestDto, request);
    }

}
