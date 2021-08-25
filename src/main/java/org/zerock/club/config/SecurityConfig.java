package org.zerock.club.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * BCryptPasswordEncoder
     * 'bcrypt'라는 해시 함수를 이용해서 패스워드를 암호화하는 설계된 클래스
     * BCryptPasswordEncoder로 만들어진 패스워드는 원래대로 복호화도 불가능하고, 암호화가 매번 다르게 바뀝니다.
     * 길이는 동일하지만,대신 특정한 문자열이 암호화 된 결과인지만을 확인할 수 있음.
     * @return
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/sample/all").permitAll()
                .antMatchers("/sample/member").hasRole("USER");

        http.formLogin();       // 인가/인증에 문제시 로그인 화면
        http.csrf().disable();  // csrf 비활성화
        http.logout();          // 로그아웃 주의할점은 CSRF 사용시 POST방식으로만 로그아웃 처리 가능
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        // id:user1, pw:1111
        auth.inMemoryAuthentication().withUser("user1")
                .password("$2a$10$bNHWMFbbSPGDmx.5.374SOoqOydpPjFL7VPlbKzb7LkzlOtodaigO")
                .roles("USER");

    }
}