package com.example.notice_test.repository;

import com.example.notice_test.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// Jpa레포를 상속받아 DB와 연결
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findAllByOrderByModifiedAtDesc(); // 데이터를 시간에 따라 내림차순으로 정리해줌

    Optional<Notice> findByIdAndUserId(Long id, Long userId);

    List<Notice> findAllByUserId(Long userId); // 유저 아이디와 동일한 게시글 가져오기
}
