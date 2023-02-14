package com.example.notice_test.dto;

import lombok.Getter;
import lombok.Setter;


import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Setter
@Getter

public class SignupRequestDto {


    @Size(min = 4, max = 10, message = "username은 최소4자, 최대 10자 이하입니다.") // 최소 4자, 최대 10자
    private String username;
    @Size(min = 8, max = 15, message = "password는 최소8자, 최대 15자 이하입니다.")
    private String password;
    private String email;
    private boolean admin = false;
    private String adminToken = "";
}
