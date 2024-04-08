package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Todo")
public class TodoEntity { // 할일목록 엔터티
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id; // 이 오브젝트의 아이디
    private String userId; // 이 오브젝트를 생성한 사용자 아이디
    private String title; // Todo 타이틀(예: 운동하기)
    private boolean done; // true - todo를 완료한 경우(checked)
}
