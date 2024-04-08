package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TodoDTO;
import com.example.demo.model.TodoEntity;
import com.example.demo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 할일목록 컨트롤러
@RestController // @Controller + @ResponseBody : Rest API 구현
@RequestMapping("/todo")
public class TodoController {

    @Autowired
    private TodoService service;

    // 할일목록 추가하기
    @PostMapping
    public ResponseEntity<?> createTodo(
            // 인증된 유저 아이디 받아오기
            @AuthenticationPrincipal String userId,
            // 입력받은 할일 목록 데이터
            @RequestBody TodoDTO dto) {
        try {
            // 1. TodoEntity로 변환한다.
            TodoEntity entity = TodoDTO.toEntity(dto);

            // 2. id를 null로 초기화하한다. 생성 당시에는 id가 없어야 하기 때문이다.
            entity.setId(null);

            // 3. 임시 사용자 아이디를 설정해 준다.
            entity.setUserId(userId);

            // 4. 서비스를 이용해 Todo 엔티티를 생성한다.
            List<TodoEntity> entities = service.create(entity);

            // 5 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환한다.
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).toList();

            // 6. 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화한다.
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            // 7. ResponseDTO를 리턴한다.
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            // 8. 혹시 예외가 있는 경우 dto 대신 error에 메시지를 넣어 리턴한다.
            String error = e.getMessage();
            ResponseDTO response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 특정 유저 기준으로 전체 목록 요청
    @GetMapping
    public ResponseEntity<?> retrieveTodoList(
            @AuthenticationPrincipal String userId) {
        // 전체 엔티티 가져오기
        List<TodoEntity> entities = service.retrieve(userId);
        // dto 수정하고 리턴하기
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).toList();
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<?> updateTodo(
            @AuthenticationPrincipal String userId,
            @RequestBody TodoDTO dto) {
        // dto -> entity -> 수정 -> entities
        TodoEntity entity = TodoDTO.toEntity(dto);
        entity.setUserId(userId);
        List<TodoEntity> entities =  service.update(entity);
        // dto 변경
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).toList();
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
        return ResponseEntity.ok().body(response);
    }

    // 할일목록 삭제하기
    @DeleteMapping
    private ResponseEntity<?> deleteTodo(
            // 인증된 사용자 아이디
            @AuthenticationPrincipal String userId,
            @RequestBody TodoDTO dto) {
        try {
            // 삭제 로직 수행
            TodoEntity entity = TodoDTO.toEntity(dto);
            entity.setUserId(userId);
            List<TodoEntity> entities = service.delete(entity);
            // 이상없으면, 전체 todo list 리턴하기
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).toList();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            // 에러 메시지 담아서 보내기
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

}
