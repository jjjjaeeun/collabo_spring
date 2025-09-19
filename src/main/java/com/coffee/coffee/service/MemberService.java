package com.coffee.coffee.service;

import com.coffee.coffee.constant.Role;
import com.coffee.coffee.entity.Member;
import com.coffee.coffee.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service // 서비스 역할을 하며, 주로 로직 처리에 활용되는 자바 클래스
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository ;

    public Member findByEmail(String email) {
        return  memberRepository.findByEmail(email);

    }

    public void insert(Member bean) {
        //사용자 역활과 등록 일자는 여기서 넣어줌
        bean.setRole(Role.USER);
        bean.setRegdate(LocalDate.now());

        // 주의) Repository에서 인서트 작업은 save() 메소드를 사용
        memberRepository.save(bean);
    }
}
