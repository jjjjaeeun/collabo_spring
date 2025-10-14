package com.coffee.coffee.service;

import com.coffee.coffee.constant.Role;
import com.coffee.coffee.entity.Member;
import com.coffee.coffee.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service // 서비스 역할을 하며, 주로 로직 처리에 활용되는 자바 클래스
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository ;

    public Member findByEmail(String email) {
        return  memberRepository.findByEmail(email);

    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void insert(Member bean) {
        //사용자 역활과 등록 일자는 여기서 넣어줌
        bean.setRole(Role.USER);
        bean.setRegdate(LocalDate.now());

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(bean.getPassword());
        bean.setPassword(encodedPassword);

        // 주의) Repository에서 인서트 작업은 save() 메소드를 사용
        memberRepository.save(bean);
    }

    public Optional<Member> findMemberById(Long memberId) {
        return this.memberRepository.findById(memberId);
    }
}
