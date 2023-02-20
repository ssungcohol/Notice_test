package com.example.notice_test.entity;

import lombok.Getter;
import net.bytebuddy.asm.Advice;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass // ??
@EntityListeners(AuditingEntityListener.class)  //??
public class Timestamped {

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

//    @CreatedDate
//    private LocalDateTime commentCreatedAt;
//
//    @LastModifiedDate
//    private LocalDateTime commentModifiedAt;

    // 생성, 수정 시간 중복 표시
}
