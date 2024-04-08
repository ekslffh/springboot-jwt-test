package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO { // 사용자 DTO 클래스 : 로그인 이후 토큰을 받아온다.
    private String token;
    private String email;
    private String username;
    private String password;
    private String id;
}
