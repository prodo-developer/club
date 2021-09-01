package org.zerock.club.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.zerock.club.security.filter.ApiCheckFilter;
import org.zerock.club.security.filter.ApiLoginFilter;
import org.zerock.club.security.handler.ClubLoginSuccessHandler;
import org.zerock.club.security.service.ClubUserDetailService;

@Configuration
@Slf4j
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true) // URL에 어노테이션 기반의 접근제한을 설정할 수 있도록 하는 설정
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ClubUserDetailService userDetailService;

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
//        http.authorizeRequests()
//                .antMatchers("/sample/all").permitAll()
//                .antMatchers("/sample/member").hasRole("USER");

        http.formLogin();       // 인가/인증에 문제시 로그인 화면
        http.csrf().disable();  // csrf 비활성화
        http.logout();          // 로그아웃 주의할점은 CSRF 사용시 POST방식으로만 로그아웃 처리 가능
        http.oauth2Login().successHandler(successHandler());
        http.rememberMe().tokenValiditySeconds(60*60*24*7).userDetailsService(userDetailService); // 7days

        http.addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(apiLoginFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public ClubLoginSuccessHandler successHandler() {
        return new ClubLoginSuccessHandler(passwordEncoder());
    }

    @Bean
    public ApiLoginFilter apiLoginFilter() throws Exception {
        ApiLoginFilter apiLoginFilter = new ApiLoginFilter("/api/login");
        apiLoginFilter.setAuthenticationManager(authenticationManager());

        return apiLoginFilter;
    }

    /**
     * ApiCheckFilter 빈등록
     * 스프링 시큐리티의 여러 필터중에서 맨 마지막 필터로 동작하는 기능
     * @return
     */
    @Bean
    public ApiCheckFilter apiCheckFilter() {
        return new ApiCheckFilter("/notes/**/*");
    }

    /**
     * ClubUserDetailService가 Bean으로 등록되면 스프링 시큐리티에서 UserDetailsService로 인식하기 때문에
     * 기존에 임시로 코드로 직접 설정한 configure(AuthenticationManagerBuilder auth) 부분을 사용하지 않도록 수정
     * @param auth
     * @throws Exception
     */
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//
//        // id:user1, pw:1111
//        auth.inMemoryAuthentication().withUser("user1")
//                .password("$2a$10$bNHWMFbbSPGDmx.5.374SOoqOydpPjFL7VPlbKzb7LkzlOtodaigO")
//                .roles("USER");
//
//    }
}