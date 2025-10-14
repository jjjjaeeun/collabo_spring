package com.coffee.coffee.entity;

import com.coffee.coffee.constant.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "이름은 필수 입력 사항입니다.")
    private String name;

    // 필수 사항이고, 절대로 동일한 값이 들어오면 안된다는 뜻
    @Column(unique = true, nullable = false)
    @NotBlank(message = "이메일은 필수 입력 사항입니다.")
    @Email(message = "올바른 이메일 형식으로 입력해 주세요.")
    private String email;

    // @Pattern은 정규 표현식을 의미
    // [asdf]: 대괄호 사이에 들어있는 글자 1개와 매칭
    @NotBlank(message = "비밀번호는 필수 입력 사항입니다.")
    @Size(min = 8, max = 255, message = "비밀 번호는 8자리 이상, 255자리 이하로 입력해 주세요.")
    @Pattern(regexp = ".*[A-Z].*", message = "비밀 번호는 대문자 1개 이상을 포함해야 합니다.")
    @Pattern(regexp = ".*[!@#$%].*", message = "비밀 번호는 특수 문자 '!@#$%' 중 하나 이상을 포함해야 합니다.")
    private String password;

    @NotBlank(message = "주소는 필수 입력 사항입니다.")
    private String address;

    @Enumerated(EnumType.STRING) // 컬럼에 문자열 형식으로 데이터가 들어감
    private Role role; // 일반인 또는 관리자

    // 자바의 객체를 json 타입으로 변경할 때 LocalDate, LocalDateTime 클래스들이 변환이 잘 이루어 지지 않습니다.
    // Jackson 라이브러리를 사용하여 이를 해결할 수 있음
    // pom.xml 파일에 Jackson 라이브러리를 추가해 주어야함
    @JsonFormat(pattern = "yyy-MM-dd") // 변환시 날짜 형식을 개발자가 지정
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
