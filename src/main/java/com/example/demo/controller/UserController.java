package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.UserEntity;
import com.example.demo.security.TokenProvider;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 유저 관련 컨트롤러
@Slf4j
@RestController
@RequestMapping("/auth")
public class UserController {

    // 회원가입, 로그인 기능 제공
    @Autowired
    UserService userService;

    // 토큰 생성 및 검증 기능 제공
    @Autowired
    TokenProvider tokenProvider;

    // 회원가입 하기
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        try {
            // 요청을 이용해 저장할 사용장 만들기
            UserEntity user = UserEntity.builder()
                    .email(userDTO.getEmail())
                    .userName(userDTO.getUsername())
                    .password(userDTO.getPassword())
                    .build();
            // 서비스를 이용해 리포지터리에 사용자 저장
            UserEntity registeredUser = userService.create(user);
            UserDTO responseUserDTO = UserDTO.builder()
                    .email(registeredUser.getEmail())
                    .id(registeredUser.getId())
                    .username(registeredUser.getUserName())
                    .build();
            // 사용자의 정보는 항상 하나이므로 리스트로 만들어야 하는 ResponseDTO를 사용하지 않고 그냥 UserDTO 리턴
            return ResponseEntity.ok().body(responseUserDTO);
        } catch (Exception e) {
            // 에러 담아서 보내기
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    // 로그인 요청
    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {
        // id, pw로 user 가져와보기
        UserEntity user = userService.getByCredentials(
                userDTO.getEmail(),
                userDTO.getPassword());
        // 로그인 성공시에
        if (user != null) {
            // 토큰 생성하고 userDTO에 담아서 보내기
            final String token = tokenProvider.create(user);
            final UserDTO responseUserDTO = userDTO.builder()
                    .email(user.getEmail())
                    .id(user.getId())
                    .token(token)
                    .build();
            return ResponseEntity.ok().body(responseUserDTO);
        } else { // 로그인 실패..
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("Login failed.")
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
