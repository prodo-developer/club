package org.zerock.club.security.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JWTTest {

    private JWTUtil jwtUtil;

    @BeforeEach
    public void testBefore() {
        System.out.println("testBefore.............");
        jwtUtil = new JWTUtil();
    }

    @Test
    @DisplayName("jwt토큰값 확인")
    public void testEncode() throws Exception {
        String email = "user95@zerock.co.kr";

        String str = jwtUtil.generateToken(email);

        System.out.println(str);
    }

    @Test
    @DisplayName("generateToken 토큰 검증")
    public void testValidate() throws Exception {

        String email = "user95@zerock.co.kr";
        String str = jwtUtil.generateToken(email);

        Thread.sleep(5000);

        String resultEmail = jwtUtil.validateAndExtract(str);

        System.out.println(resultEmail);
    }
}