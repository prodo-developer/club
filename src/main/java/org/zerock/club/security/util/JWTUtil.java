package org.zerock.club.security.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultJws;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * JWT Util
 * 1. 인증에 성공했을 때 JWT 문자열을 만들어서 클라이언트에게 전송
 * 2. 클라이언트가 보낸 토큰의 값을 검증하는 경우에 사용
 *
 */
@Slf4j
public class JWTUtil {

    private String secretKey = "zerock12345678";

    //1month
    private long expire = 60 * 24 * 30;

    /**
     * JWT 토큰 생성
     * JWT문자열 자체를 알면 누구든 API를 사용할 수 있다는 문제가 있어,  Signature를 생성합니다. (30일기준)
     * @param content
     * @return
     * @throws Exception
     */
    public String generateToken(String content) throws Exception {
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expire).toInstant()))
//                .setExpiration(Date.from(ZonedDateTime.now().plusSeconds(1).toInstant()))
                .claim("sub", content)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes("UTF-8"))
                .compact();
    }

    /**
     * 인코딩된 문자열에서 원하는 값을 추출하는 용도
     * 만료기간이 지난것이라면 Exception 발생
     * @param tokenStr
     * @return
     * @throws Exception
     */
    public String validateAndExtract(String tokenStr) throws Exception {
        String contenValue = null;

        try {
            DefaultJws defaultJws = (DefaultJws) Jwts.parser()
                        .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(tokenStr);

            log.info(String.valueOf(defaultJws));

            log.info(String.valueOf(defaultJws.getBody().getClass()));

            DefaultClaims claims = (DefaultClaims) defaultJws.getBody();

            log.info("-----------------------");

            contenValue = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            contenValue = null;
        }
        return contenValue;
    }
}