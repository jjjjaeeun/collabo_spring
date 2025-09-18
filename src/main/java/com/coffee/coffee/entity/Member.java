package com.coffee.coffee.entity;

import com.coffee.coffee.constant.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Entity // 해당 클래스를 엔터티로 관리
@Table(name = "members") // 테이블 이름은 members로 생성
// 회원 한명에 대한 정보를 저장하고 있는 자바 클래스
public class Member {
    @Id // 이 컬럼은 primary key
    @GeneratedValue(strategy = GenerationType.AUTO) // 기본키 생성 전략
    @Column(name = "member_id") // 컬럼 이름을 변경
    private Long id;


    private String name;

    // 필수 사항이고, 절대로 동일한 값이 들어오면 안된다는 뜻
    @Column(unique = true, nullable = false)
    private String email;


    private String password;


    private String address;

    @Enumerated(EnumType.STRING) // 컬럼에 문자열 형식으로 데이터가 들어감
    private Role role; // 일반인 또는 관리자


    private LocalDate regdate; // 등록 일자

    public Member() {
    }

    public Member(Long id, String name, String email, String password, String address, Role role, LocalDate regdate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.role = role;
        this.regdate = regdate;
    }
}
