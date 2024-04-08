package com.example.demo.service;

import com.example.demo.model.UserEntity;
import com.example.demo.persistence.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// 유저 관리 서비스
@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 유저 생성 : 화원가입
    public UserEntity create(final UserEntity userEntity) {
        // 유저 엔터티가 비어있거나 email이 없으면 에러
        if (userEntity == null || userEntity.getEmail() == null) {
            throw new RuntimeException("Invalid arguments");
        }
        final String email = userEntity.getEmail();
        // 이메일 중복 검사
        if (userRepository.existsByEmail(email)) {
            log.warn("Email already exists {}", email);
            throw new RuntimeException("Email already Exists");
        }
        // 저장한 엔터티 리턴하기
        return userRepository.save(userEntity);
    }

    // 로그인 처리
    public UserEntity getByCredentials(final String email, final String password) {
        // id, pw로 저장소에서 데이터 가져와 보기 : 있으면 성공, else 실패
        return userRepository.findByEmailAndPassword(email, password);
    }
}
