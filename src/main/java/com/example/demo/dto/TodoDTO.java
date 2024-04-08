package com.example.demo.dto;

import com.example.demo.model.TodoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TodoDTO { // 할일목록 DTO, 데이터 바인딩시에 기본생성자, setter, getter 필요하다.
    private String id;
    private String title;
    private boolean done;

    // Entity -> DTO : 데이터베이스로부터 받아온 엔터티를 DTO로 변경
    public TodoDTO(final TodoEntity entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.done = entity.isDone();
    }

    // DTO -> Entity : 사용자로부터 받아온 DTO를 엔터티로 변경
    public static TodoEntity toEntity(final TodoDTO dto) {
        return TodoEntity.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .done(dto.isDone())
                .build();
    }
}
