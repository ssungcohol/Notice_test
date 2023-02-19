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
    @GetMapping ("/api/notice/{id}/comments")
    public List<CommentResponseDto> getComment(@PathVariable Long id) {
        return commentService.getComment(id);
    }


    // 댓글 작성
    @PostMapping("/api/notice/{id}/comment")
    public CommentResponseDto creatComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {

        return commentService.creatComment(id, requestDto, request);
    }

    // 댓글 수정
    @PutMapping("api/notice/{id}/comment")
    public CommentResponseDto updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {

        return commentService.updateComment(id, requestDto, request);
    }


    // 댓글 수정
    @DeleteMapping("/api/notice/{id}/comment")
    public CommentMessageDto deleteComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {

        return commentService.deleteComment(id, requestDto, request);
    }

}
