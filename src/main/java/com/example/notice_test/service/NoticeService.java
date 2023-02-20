package com.example.notice_test.service;

import com.example.notice_test.dto.CommentResponseDto;
import com.example.notice_test.dto.NoticeMessageDto;
import com.example.notice_test.dto.NoticeRequestDto;
import com.example.notice_test.dto.NoticeResponseDto;
import com.example.notice_test.entity.Comment;
import com.example.notice_test.entity.Notice;
import com.example.notice_test.entity.User;
import com.example.notice_test.entity.UserRoleEnum;
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


@Service  // 이곳은 DB와 연결을 해주는 서비스다! 라는걸 알려줌
@RequiredArgsConstructor
public class NoticeService {  // 이곳에서 데이터베이스와 연결을 해줌

    private final NoticeRepository noticeRepository;  // 데이터와 연결하는 부분인 NoticeRepository를 사용할 수 있도록 추가
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    private final CommentRepository commentRepository;

    // 게시글 목록 조회
    public List<NoticeResponseDto> getNotice() {
        List <Notice> noticeList = noticeRepository.findAllByOrderByModifiedAtDesc(); // 리스트 형태로 내림차순 정리
        List<Comment> commentList = new ArrayList<>();
        List <NoticeResponseDto> noticeResponseDtoList = new ArrayList<>();
        for (Notice notice : noticeList) {
            for (Comment comment : notice.getComments()) {
                commentList.add(comment);
            }
            commentList = commentRepository.findAllByOrderByModifiedAtDesc();
            noticeResponseDtoList.add(new NoticeResponseDto(notice, commentList));
        }
        return noticeResponseDtoList;
    }

//    List<Comment> commentList = new ArrayList<>(notice.getComments());


//==================================================================================================
    // 게시글 작성
//    @Transactional
//    public NoticeResponseDto createNotice(NoticeRequestDto requestDto){
//        Notice notice = new Notice(requestDto);
//        noticeRepository.save(notice);
//        NoticeResponseDto noticeResponseDto = new NoticeResponseDto(notice);
//        return noticeResponseDto;
//    }


    @Transactional
    public NoticeResponseDto createNotice(NoticeRequestDto requestDto, HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 게시글 추가 가능
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

            // 요청받은 DTO 로 DB에 저장할 객체 만들기
            Notice notice = noticeRepository.saveAndFlush(new Notice(requestDto, user));


            return new NoticeResponseDto(notice, null);
            // 작성시에는 댓글이 없음

        } else {
            return null;
        }
    }

//    =====================================================================================

//     선택 게시글 조회
    @Transactional (readOnly = true)
    public NoticeResponseDto getNotice(Long id) {
        Notice notice = noticeRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );

//        Comment comment = commentRepository.findById(id).orElseThrow(
//                () -> new IllegalArgumentException("해당 게시물에 댓글이 존재하지 않습니다.")
//        );

        List<Comment> commentList = new ArrayList<>(notice.getComments());
        // 게시글의 댓글만 가져오기

        NoticeResponseDto noticeResponseDto = new NoticeResponseDto(notice, commentList);
        return noticeResponseDto;
    }

    // 회원(토큰)게시글 조회
//    @Transactional(readOnly = true)  // 조회 기능이 붙어 readOnly 적용
//    public List<NoticeResponseDto> getNotice(Long id) {
//        // Request에서 Token 가져오기 Header
//        String token = jwtUtil.resolveToken(Long id);
//        Claims claims;
//
//        // 토큰이 있는 경우에만 자신의 게시글 조회 가능
//        if(token != null) {
//            // Token 검증
//            if (jwtUtil.validateToken(token)) {
//                // 토큰에서 사용자 정보 가져오기
//                claims = jwtUtil.getUserInfoFromToken(token);
//            } else {
//                throw new IllegalArgumentException("Token Error");
//            }
//
//            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
//            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
//                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
//            );
//
//            // 사용자 권한 가져와서 ADMIN 이면 전체 조회, USER 면 본인이 추가한 부분 조회
//            UserRoleEnum userRoleEnum = user.getRole();
//            System.out.println("role = " + userRoleEnum);
//
//            List<NoticeResponseDto> list = new ArrayList<>();
//            List<Notice> noticeList;
//
//            if (userRoleEnum == UserRoleEnum.USER) {
//                // 사용자 권한이 USER일 경우
//                noticeList = noticeRepository.findAllByUserId(user.getId());
////                noticeList = noticeRepository.findAllByOrderByModifiedAtDesc(); // 내림차순 조회 가능?
////                noticeList = noticeRepository.findByIdAndUserId(Long id, Long userId);
//            } else {
//                noticeList = noticeRepository.findAll(); // 관리자일 경우에는 다 보여주기
//            }
//
//            for (Notice notice : noticeList) {
//                list.add(new NoticeResponseDto(notice));
//            }
//
//            return list;
//
//        }else {
//            return null;
//        }
//    }

//==========================================================================================================

    // 게시글 수정 및 비밀번호 조회
//    @Transactional
//    public NoticeMessageDto updateNotice(Long id, NoticeRequestDto requestDto) {  // 컨트롤러랑 타입 맞추는거 잊지말기
//        Notice notice = noticeRepository.findById(id).orElseThrow(
//                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
//        );
//        if (requestDto.getPassword().equals(notice.getPassword())) {  //비밀번호가 일치하면
//            notice.update(requestDto);
//            return new NoticeMessageDto("수정 성공");  //생성자를 생성하여 NoticeMessageDTO에 전달
//        }
//        return new NoticeMessageDto("수정 실패");
//    }

    // 회원(토큰) 게시글 수정 및 토큰 확인
    @Transactional
    public NoticeResponseDto updateNotice(Long id, NoticeRequestDto requestDto, HttpServletRequest request) {
        // NoticeResponseDto 타입! Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 게시물 업데이트 가능
        if (token != null) {
            // Token 검증
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

            // 게시물이 있는지 없는지, 게시물의 id도 가지고 와서 게시물이 있는지 없는지 확인 했음
            Notice notice = noticeRepository.findById(id).orElseThrow(
                    () -> new NullPointerException("해당 게시물이 존재하지 않습니다.")
            );

            List<Comment> commentList = new ArrayList<>(notice.getComments());
            // 게시글 id 값의 댓글만 가져오면 됨 List 형태로


            // Admin 조건 추가해주기 & 모든 게시글 수정 가능
            if (user.getRole().equals(UserRoleEnum.ADMIN) || notice.getUser().getUsername().equals(user.getUsername())) {

                notice.update(requestDto);

                return new NoticeResponseDto(notice, commentList); // 수정된 게시글 반환

            } else return null;

        } else {
            return null;
        }
    }

//    =====================================================================================================
    // 게시글 삭제
//    @Transactional
//    public NoticeMessageDto delete(Long id, NoticeRequestDto requestDto) {
//        Notice notice = noticeRepository.findById(id).orElseThrow(
//                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
//        );
//        if (requestDto.getPassword().equals(notice.getPassword())) {  //비밀번호가 일치하면
//            noticeRepository.deleteById(id); // 게시물을 삭제하거라~~
//            return new NoticeMessageDto("삭제 성공");
//        }
//        else {
//            return new NoticeMessageDto("맘대로 못지우지~~");
//        }
//    }


    // 회원(토큰) 게시글 삭제
    @Transactional
    public NoticeMessageDto deleteNotice(Long id, NoticeRequestDto requestDto, HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 관심상품 최저가 업데이트 가능
        if (token != null) {
            // Token 검증
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

            Notice notice = noticeRepository.findById(id).orElseThrow(
                    () -> new NullPointerException("해당 게시물이 존재하지 않습니다.")
            );

            if (user.getRole() == UserRoleEnum.ADMIN || notice.getUser().getUsername().equals(user.getUsername())) {

                noticeRepository.deleteById(id);

                return new NoticeMessageDto("삭제 성공");
            } else {
                return new NoticeMessageDto("삭제 실패");
            }

        }
        return new NoticeMessageDto("삭제 실패");
    }

}