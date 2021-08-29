package org.zerock.club.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zerock.club.security.dto.ClubAuthMemberDTO;

@Controller
@Slf4j
@RequestMapping("/sample/")
public class SampleController {

    /**
     * 로그인을 하지 않은 사용자 접근
     */
    @PreAuthorize("permitAll()")
    @GetMapping("/all")
    public void exAll() {
        log.info("exAll......");
    }

    /**
     * 로그인한 사용자만이 접근
     */
    @GetMapping("/member")
    public void exMember(@AuthenticationPrincipal ClubAuthMemberDTO clubAuthMember) {
        log.info("exMember......");

        log.info("--------------------");
        log.info(String.valueOf(clubAuthMember));
    }

    /**
     * 관리자권한이 있는 사용자 접근
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public void exAdmin() {
        log.info("exAdmin......");
    }

    @PreAuthorize("#clubAuthMember != null && #clubAuthMember.username eq \"user95@zerock.co.kr\"")
    @GetMapping("/exOnly")
    public String exMemberOnly(@AuthenticationPrincipal ClubAuthMemberDTO clubAuthMember) {
        log.info("exMemberOnly................");
        log.info(String.valueOf(clubAuthMember));

        return "/sample/admin";
    }
}