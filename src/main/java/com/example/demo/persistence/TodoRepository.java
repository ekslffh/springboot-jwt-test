package com.example.demo.persistence;

import com.example.demo.model.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// 할일목록 저장소
@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, String> {
    // 특정 유저 아이디로 할일목록들 가져오기
    List<TodoEntity> findByUserId(String userId);
}
