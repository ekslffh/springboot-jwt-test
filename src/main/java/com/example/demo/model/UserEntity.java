package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "email")}) // 중복값 제한
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id; // 사용자에게 고유하게 부여되는 id

    @Column(nullable = false)
    private String userName; // 사용자의 이름

    @Column(nullable = false, unique = true)
    private String email; // 사용자의 email, 아이디와 같은 기능

    @Column(nullable = false)
    private String password; // 패스워드
}
