package org.zerock.club.security.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JWTTest {

    private JWTUtil jwtUtil;

    @BeforeEach
    public void testBefore() {
        System.out.println("testBefore.............");
        jwtUtil = new JWTUtil();
    }

    @Test
    public void testEncode() throws Exception {
        String email = "user95@zerock.co.kr";

        String str = jwtUtil.generatetoken(email);

        System.out.println(str);
    }
}