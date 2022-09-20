package com.project.crux.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.crux.common.ResponseDto;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPointException implements
        AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException e) throws IOException {

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().print( new ObjectMapper().writeValueAsString(
                ResponseDto.fail("UNAUTHORIZED", "로그인이 필요합니다.")
        ));
    }
}
