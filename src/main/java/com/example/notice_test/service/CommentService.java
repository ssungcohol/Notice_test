package com.example.notice_test.service;

import com.example.notice_test.dto.CommentMessageDto;
import com.example.notice_test.dto.CommentRequestDto;
import com.example.notice_test.dto.CommentResponseDto;
import com.example.notice_test.entity.Comment;
import com.example.notice_test.entity.Notice;
import com.example.notice_test.entity.User;
import com.example.notice_test.jwt.JwtUtil;
import com.example.notice_test.repository.CommentRepository;
import com.example.notice_test.repository.NoticeRepository;
import com.example.notice_test.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service // 나 서비스임
@RequiredArgsConstructor // 내가 생성자 대신 만들어줌
public class CommentService {

    private final CommentRepository commentRepository; // 레포 연결

    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;

    private final NoticeRepository noticeRepository;


    // 댓글 조회 => 게시글 id를 가지고와서 해당 게시글의 댓글만 표시
    public List<CommentResponseDto> getComment(Long id) {
        List <Comment> commentList = commentRepository.findAllByOrderByCommentModifiedAtDesc();
        List <CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            CommentResponseDto tmp = new CommentResponseDto(comment);
            commentResponseDtoList.add(tmp);
        }
        return commentResponseDtoList;
    }



    // 댓글 작성
    @Transactional
    public CommentResponseDto creatComment(Long id, CommentRequestDto requestDto, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 댓글 작성 가능
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            // 게시글 DB 유무 확인 필요
            Notice notice = noticeRepository.findById(id).orElseThrow(
                    () -> new NullPointerException("해당 게시물이 없습니다.")
            );


            // 요청받은 DTO로 DB에 저장할 객체 만들기
            Comment comment = commentRepository.saveAndFlush(new Comment(requestDto, user, notice));

            return new CommentResponseDto(comment);

        } else {
            return null;
        }

    }


    // 댓글 수정 해당 사용가자 작성한 댓글만 수정 가능 & 댓글 DB 파악 여부 확인
    @Transactional
    public CommentResponseDto updateComment(Long id, CommentRequestDto requestDto, HttpServletRequest request) {


        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰 검증 후 수정 (여부 파악)
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
                // throw 키워드 사용하여 예외 발생시켜줌
            }

            // 토큰에서 가져온 사용자 정보를 조회하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            // 게시물 여부 파악 (DB에 게시물이 있나 없나 게시물 id 조회)
            Notice notice = noticeRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다.")
            );

            // 댓글 여부 파악 (ID 조회 필요)
            Comment comment = commentRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
            );

            comment.update(requestDto);

            return new CommentResponseDto(comment);

        } else return null;

    }




    // 댓글 삭제
    @Transactional
    public CommentMessageDto deleteComment(Long id, CommentRequestDto requestDto, HttpServletRequest request) {
        // 1. jwt Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 댓글 삭제 가능하게
        // Token 검증
        if (token != null) {
            if(jwtUtil.validateToken(token)) {

                claims = jwtUtil.getUserInfoFromToken(token);

            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // Token에서 가져온 사용자 정보를 사용하여 DB 조회

            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            Notice notice = noticeRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다.")
            );

            commentRepository.deleteById(id);

            return new CommentMessageDto("댓글 삭제 성공");
        } else return new CommentMessageDto("댓글 삭제 실패");
    }
}
