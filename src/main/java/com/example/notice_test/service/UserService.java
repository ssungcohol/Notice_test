package com.example.notice_test.service;


import com.example.notice_test.dto.LoginRequestDto;
import com.example.notice_test.dto.SignupRequestDto;
import com.example.notice_test.entity.User;
import com.example.notice_test.entity.UserRoleEnum;
import com.example.notice_test.jwt.JwtUtil;
import com.example.notice_test.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service // 나 서비스요
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    // userRepository 와 연결

    // ADMIN TOKEN 지정
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    //Jwt Util 의존성 주입
    private final JwtUtil jwtUtil;

    public void signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();
        String email = signupRequestDto.getEmail();

        // 회원 중복 여부 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // 일반, 관리자 확인
        UserRoleEnum role = UserRoleEnum.USER; // userRollEnum 에 주고나서
        if(signupRequestDto.isAdmin()){ // admin이 있는지 없는지 확인
            if(!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) { //admin true 라면
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능 합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        User user = new User(username, password, email, role);
        userRepository.save(user);
    }

    // 토큰 수정 전
//    @Transactional
//    public void login(LoginRequestDto loginRequestDto) {
//        String username = loginRequestDto.getUsername();
//        String password = loginRequestDto.getPassword();
//
//        // 사용자 확인
//        User user = userRepository.findByUsername(username).orElseThrow(
//                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
//        );
//
//        // 비밀번호 확인
//        if(!user.getPassword().equals(password)) {
//            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
//        }
//    }
//
//}
//  토큰 수정 후
    @Transactional(readOnly = true)
    public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );
        // 비밀번호 확인
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));
    }
}
