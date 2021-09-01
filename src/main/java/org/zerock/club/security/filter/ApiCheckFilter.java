package org.zerock.club.security.filter;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class ApiCheckFilter extends OncePerRequestFilter {

    private AntPathMatcher antPathMatcher;
    private String pattern;

    public ApiCheckFilter(String pattern) {
        this.antPathMatcher = new AntPathMatcher();
        this.pattern = pattern;
    }

    /**
     * ApiCheckFilter 동작을 위한 URL
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("REQUESTURI: " + request.getRequestURI());
        log.info(String.valueOf(antPathMatcher.match(pattern, request.getRequestURI())));

        if(antPathMatcher.match(pattern, request.getRequestURI())) {

            log.info("ApiCheckFilter..............................");
            log.info("ApiCheckFilter..............................");
            log.info("ApiCheckFilter..............................");

            boolean checkHeader = checkAuthHeader(request);

            if(checkHeader) {
                filterChain.doFilter(request, response);
                return;
            } else { // Authorization가 없는 경우 메시지 출력(헤더 검증 실패처리)
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                // json 리턴 및 한글깨짐 수정.
                response.setContentType("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                String message ="FAIL CHECK API TOKEN";
                json.put("code", "403");
                json.put("message", message);

                PrintWriter out = response.getWriter();
                out.print(json);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Authorization 헤더 처리
     * @param request
     * @return
     */
    private boolean checkAuthHeader(HttpServletRequest request) {

        boolean checkResult = false;

        String authHeader = request.getHeader("Authorization");

        if(StringUtils.hasText(authHeader)) {
            log.info("Authorization header Value: " + authHeader);
            if(authHeader.equals("12345678")) {
                checkResult = true;
            }
        }

        return checkResult;
    }
}