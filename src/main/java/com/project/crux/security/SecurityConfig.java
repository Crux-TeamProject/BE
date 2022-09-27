package com.project.crux.security;

import com.project.crux.security.jwt.JwtAuthenticationEntryPointException;
import com.project.crux.security.jwt.JwtFilter;
import com.project.crux.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@ConditionalOnDefaultWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SecurityConfig {

    @Value("${jwt.secret}")
    String SECRET_KEY;
    private final TokenProvider tokenProvider;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final JwtAuthenticationEntryPointException jwtAuthenticationEntryPointException;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // cors 설정
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("POST","GET","DELETE","PUT"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader("Access_Token");
        configuration.addExposedHeader("Authorization");
        configuration.addExposedHeader("Last-Event-ID");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors();

        http.csrf().disable()


                // 시큐리티 예외 처리
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPointException)

                // jwt 토큰 사용하므로 세션 생성 X
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()

                // preflight request 허용
                .mvcMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // 회원가입 관련 filter 통과
                .antMatchers("/members/login").permitAll()
                .antMatchers("/members/email-check").permitAll()
                .antMatchers("/members/nickname-check").permitAll()
                .antMatchers("/members/signup").permitAll()
                .antMatchers(HttpMethod.GET,"/gyms/**").permitAll()
                .antMatchers(HttpMethod.GET, "/crews/*/posts").permitAll()
                .antMatchers(HttpMethod.GET, "/crews/*").permitAll()
                .antMatchers(HttpMethod.GET, "/crews").permitAll()

                .antMatchers(HttpMethod.GET, "/crews/*/members").authenticated()

                .antMatchers("/oauth/**").permitAll()

                .antMatchers(HttpMethod.GET, "/stomp/chat/**").permitAll()
                .anyRequest().authenticated()

                .and()
                .addFilterBefore(new JwtFilter(SECRET_KEY, tokenProvider, userDetailsServiceImpl), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
}