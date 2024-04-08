package com.example.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// 스프링 필터 : 디스패쳐 서블릿 전에 수행되며, 이를 통해서 사용자의 토큰을 검증하는 역할을 수행한다.
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // 토큰 검증하는 역할
    @Autowired
    private TokenProvider tokenProvider;

    // 메서드 오버라이딩 수행
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 요청에서 토큰 가져오기
            String token = parseBearerToken(request);
            // 필터 동작 확인
            log.info("Filter is running...");
            // 토큰 검사하기. JWT이므로 인증 서버에 요청하지 않고도 검증 가능
            if (token != null && !token.equalsIgnoreCase("null")) {
                // 검증 후, userId 가져오기. 위조된 경우 예외 처리된다.
                String userId = tokenProvider.validateAndGetUserId(token);
                log.info("Authenticated user ID : " + userId);
                // 인증 완료. SecurityContextHolder에 등록해야 인증된 사용자라고 생각한다.
                AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userId, // 인증된 사용자 정보, 보통 UserDetails 타입을 넣는다.
                        null, // 사용자 비밀번호
                        AuthorityUtils.NO_AUTHORITIES // 특정 권한 없음. (처리X)
                );
                // 현재 요청에 대한 세부사항 저장
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 인증객체 만들어서 ContextHolder에 등록하기
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authentication);
                // 등록완료
                SecurityContextHolder.setContext(securityContext);
            }
        } catch (Exception ex) {
            logger.error("Colud not set user authentication in security context", ex);
        }

        // 다음 스프링 필터 수행하기
        filterChain.doFilter(request, response);
    }

    // Authorization 헤더에서 토큰값 추출해내는 메서드
    private String parseBearerToken(HttpServletRequest request) {
        // HTTP 요청의 헤더를 파싱해 Bearer 토큰을 리턴한다.
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // 'Bearer ' 띄고 다음부터
        }
        // 없는 경우 Null
        return null;
    }
}
