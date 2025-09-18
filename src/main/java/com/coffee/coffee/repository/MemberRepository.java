package com.coffee.coffee.repository;

import com.coffee.coffee.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

// 회원 정보들을 이용하여 데이터 베이스와 교신하는 인터페이스
// 이전의 Dao 역할
// JpaRepository< 엔터티 이름, 해당 엔터티의 기본키 변수 타입>
public interface MemberRepository extends JpaRepository<Member, Long> {
}
