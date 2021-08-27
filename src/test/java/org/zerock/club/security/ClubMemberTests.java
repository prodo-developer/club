package org.zerock.club.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zerock.club.entity.ClubMember;
import org.zerock.club.entity.ClubMemberRole;
import org.zerock.club.repository.ClubMemberRepository;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
class ClubMemberTests {

    @Autowired
    private ClubMemberRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("ClubMember 회원 등록")
    public void insertDummies() {

        // 1- 80까지는 USER만 지정
        // 81-90까지는 USER,MANAGER
        // 91-100까지는 USER,MANAGER,ADMIN

        IntStream.rangeClosed(1,100).forEach(i -> {
            ClubMember clubMember = ClubMember.builder()
                    .email("user" + i + "@zerock.co.kr")
                    .name("사용자" + i)
                    .fromSocial(false)
                    .password(passwordEncoder.encode("1111"))
                    .build();

            // default role
            clubMember.addMemberRole(ClubMemberRole.USER);

            if(i > 80) {
                clubMember.addMemberRole(ClubMemberRole.MANAGER);
            }

            if(i > 90) {
                clubMember.addMemberRole(ClubMemberRole.ADMIN);
            }

            repository.save(clubMember);
        });
    }

    @Test
    @DisplayName("회원 데이터 조회 테스트")
    public void testRead() {
        Optional<ClubMember> result = repository.findByEmail("user95@zerock.co.kr", false);

        ClubMember clubMember = result.get();

        System.out.println(clubMember);
    }
}