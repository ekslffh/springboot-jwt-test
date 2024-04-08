package com.example.demo.service;

import com.example.demo.model.TodoEntity;
import com.example.demo.persistence.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// 할일목록 서비스
@Slf4j
@Service
public class TodoService {

    // 저장소 주입받기.
    @Autowired
    private TodoRepository repository;

    // 할일목록 생성
    public List<TodoEntity> create(final TodoEntity entity) {
        // 검증
        validate(entity);
        // 저장
        repository.save(entity);
        log.info("Entity Id : {} is saved.", entity.getId());
        // 새롭게 추가된 전체 리스트 리턴
        return repository.findByUserId(entity.getUserId());
    }

    // 유저 아이디 기준으로 전체 목록 가져오기
    public List<TodoEntity> retrieve(final String userId) {
        return repository.findByUserId(userId);
    }

    // 할일목록 수정하기
    public List<TodoEntity> update(final TodoEntity entity) {
        // 검증
        validate(entity);
        // 수정할 특정 할일 데이터베이스에서 가져오기
        final Optional<TodoEntity> original = repository.findById(entity.getId());
        // 있으면, 수정하기
        original.ifPresent(todo -> {
            todo.setTitle(entity.getTitle());
            todo.setDone(entity.isDone());
            repository.save(todo);
        });
        // 전체 목록 리턴
        return retrieve(entity.getUserId());
    }

    // 할일목록 삭제 메서드
    public List<TodoEntity> delete(final TodoEntity entity) {
        // 검증
        validate(entity);
        try {
            // 삭제해보기
            repository.delete(entity);
        } catch (Exception e) {
            // exception handling
            log.error("error deleting entity ID : {}", entity.getId(), e);
            throw new RuntimeException("error deleting entity " + entity.getId());
        }
        // 이상 없으면, 전체 목록 리턴
        return retrieve(entity.getUserId());
    }

    // 검증 메서드
    private void validate(final TodoEntity entity) {
        // 엔터티가 비어있다면, 에러
        if (entity == null) {
            log.warn("Entity cannot be null");
            throw new RuntimeException("Entity cannot be null.");
        }

        // 엔터티에 유저아이디가 없다면, 에러
        if (entity.getUserId() == null) {
            log.warn("Unknown user.");
            throw new RuntimeException("Unknown user.");
        }
    }

}
