package com.project.crux.security.jwt;

import com.project.crux.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.Collection;
import java.util.Collections;


@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    public static String AUTHORIZATION_HEADER = "Authorization";

    public static String BEARER_PREFIX = "Bearer ";


    private final String SECRET_KEY;
    private final TokenProvider tokenProvider;
    private final UserDetailsServiceImpl userDetailsServiceImpl;


    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        // 헤더의 token 값 가져와서 jwt 넣기
        String jwt = resolveToken(request);

        // if문 jwt token 유효성 검사
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {

            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();

            //claims 에는 token 의 Payload 에 들어있는 정보
            String subject = claims.getSubject();
            Collection<? extends GrantedAuthority> authorities = Collections.EMPTY_LIST;

            // subject에는 username 담겨있고 이를 이용해서 userDetailsService에서 UserDetails 반환
            UserDetails principal = userDetailsServiceImpl.loadUserByUsername(subject);

            //인터페이스 authentication 구현한 UsernamePasswordAuthenticationToken 객체 생성
            Authentication authentication = new UsernamePasswordAuthenticationToken(principal, jwt, authorities);

            // SecurityContextHolder 안의  SecurityContext 에 authentication 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }


        // 다음 필터 진행
        filterChain.doFilter(request, response);
    }

    //header에 Access-Token 키 값을 가지는 value 값을 return , 이때 Bearer 뒤의 값만 return
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
