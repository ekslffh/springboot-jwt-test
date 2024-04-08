package com.example.demo.persistence;

import com.example.demo.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> { // 유저 저장소
    // 이메일로 유저 찾기
    UserEntity findByEmail(String email);
    // 중복검사 : 이메일 존재 여부 확인
    Boolean existsByEmail(String email);
    // 로그인 : id, pw로 계정 가져오기
    UserEntity findByEmailAndPassword(String email, String password);
}
