package com.example.notice_test.controller;

import com.example.notice_test.dto.NoticeMessageDto;
import com.example.notice_test.dto.NoticeRequestDto;
import com.example.notice_test.dto.NoticeResponseDto;
import com.example.notice_test.entity.Notice;
import com.example.notice_test.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.hibernate.internal.build.AllowPrintStacktrace;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService; // NoticeSercive와 연결하여 불러오기~(연결)


    // 게시글 목록 조회
    @GetMapping("/api/notices")
    public List<NoticeResponseDto> getNotice() {
        return noticeService.getNotice();
    }


//    ============================================================================================

    // 게시글 작성
//    @PostMapping("/api/notice")
//    public NoticeResponseDto createNotice(@RequestBody NoticeRequestDto requestDto) {
//        //createNotice 메서드 생성 클라이언트에서 가지고 온 값 사용을 위해 파라미터입력
//        return noticeService.createNotice(requestDto);
//        // 게시글을 작성하면 바로 게시글을 클라로 보내줄 것입니다!~
//    }

    // 게시글 등록(jwt)
    @PostMapping("/api/notice")
    public NoticeResponseDto createProduct(@RequestBody NoticeRequestDto requestDto, HttpServletRequest request) {
        // 응답 보내기
        return noticeService.createNotice(requestDto, request);
    }


//    =================================================================================
    //선택 게시글 조회
//    @GetMapping("/api/notice/{id}")
//    public NoticeResponseDto getNotice(@PathVariable Long id) {
//        return noticeService.getNotice(id);
//    }

    // 회원(토큰) 게시글 조회
    @GetMapping("/api/notice/{id}")
    public NoticeResponseDto getNotice(@PathVariable Long id) {
        return noticeService.getNotice(id);
    }

//    ====================================================================================

    // 선택 게시글 수정
//    @PutMapping("/api/notice/{id}")
//    public NoticeMessageDto update(@PathVariable Long id, @RequestBody NoticeRequestDto requestDto) {
//        return noticeService.update(id, requestDto);
//    }

    // 회원(토큰) 게시글 수정
    @PutMapping("/api/notice/{id}")
    public NoticeResponseDto updateNotice(@PathVariable Long id, @RequestBody NoticeRequestDto requestDto, HttpServletRequest request) {
        // 메세지 반환이 아닌 수정된 게시글을 반환해야하기에 NoticeResponseDto로 타입 변환
        return noticeService.updateNotice(id, requestDto, request);
    }


//    =====================================================================================

    // 선택 게시글 삭제
//    @DeleteMapping("/api/notice/{id}")
//    public NoticeMessageDto delete(@PathVariable Long id, @RequestBody NoticeRequestDto requestDto) {
//        // 서비스단이랑 변수 타입 맞추는거 잊지말기
//        return noticeService.delete(id, requestDto);
//    }


    // 회원(토큰) 게시글 삭제
    @DeleteMapping("/api/notice/{id}")
    // 삭제 Delete 방식 매핑
    public NoticeMessageDto deleteNotice(@PathVariable Long id, @RequestBody NoticeRequestDto requestDto, HttpServletRequest request) {
        return noticeService.deleteNotice(id, requestDto, request);
    }


    // 게시글 수정
//    @PutMapping("/api/notice/{id}")
//    public Long updateNotice(@PathVariable Long id, @RequestBody NoticeRequestDto requestDto) {
//        return noticeService.update(id, requestDto);
//    }

//    @PutMapping("/api/notice/{id}")
//    public Long updateNotice(@PathVariable Long id, @RequestBody String title, String username, String contents, String password) {
//        return noticeService.update(id, title, username, contents, password);
//    }
}
