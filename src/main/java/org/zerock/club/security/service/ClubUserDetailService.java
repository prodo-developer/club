package org.zerock.club.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.zerock.club.entity.ClubMember;
import org.zerock.club.repository.ClubMemberRepository;
import org.zerock.club.security.dto.ClubAuthMemberDTO;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Service를 사용해서 자동으로 스프링에서 빈으로 처리될 수 있게 되는점
 * loadUserByUsername()에서는 별도의 처리없이 로그를 기록
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClubUserDetailService implements UserDetailsService {

    private final ClubMemberRepository clubMemberRepository;

    /**
     * clubMemberRepository를 주입받을 수 있는 구조로 변경
     * username이 실제로는 ClubMember에서는 email을 의미하므로 이를 이용해서 ClubMemberRepository의 findByEmail 호출(소설여부 false)
     * 사용자가 존재하지 않으면 예외처리
     * ClubMember를 UserDetails 타입으로 처리하기 위해서 CLubAuthMemberDTO 타입으로 변환
     * ClubMemberRole은 스프링 시큐리티에서 사용하는 SimpleGrantedAuthority로 변환, 이때 'ROLE_' 라는 접두어 추가해서 사용
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("ClubUserDetailService loadUserByUsername " + username);

        Optional<ClubMember> result = clubMemberRepository.findByEmail(username, false);

        // isPresent() 메소드를 사용하여 Optional 객체에 저장된 값이 null인지 아닌지를 먼저 확인
        if(!result.isPresent()) {
            throw new UsernameNotFoundException("Check Email or Social");
        }

        ClubMember clubMember = result.get();

        log.info("-------------------");
        log.info(String.valueOf(clubMember));

        ClubAuthMemberDTO clubAuthMember = new ClubAuthMemberDTO(
                clubMember.getEmail(),
                clubMember.getPassword(),
                clubMember.isFromSocial(),
                clubMember.getRoleSet().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name())).collect(Collectors.toSet())
        );

        clubAuthMember.setName(clubAuthMember.getName());

        return clubAuthMember;
    }
}