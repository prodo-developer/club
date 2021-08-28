package org.zerock.club.controller;

import lombok.extern.slf4j.Slf4j;
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
    @GetMapping("/admin")
    public void exAdmin() {
        log.info("exAdmin......");
    }
}