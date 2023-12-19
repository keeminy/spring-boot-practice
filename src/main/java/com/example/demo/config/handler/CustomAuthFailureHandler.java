package com.example.demo.config.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class CustomAuthFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

        log.info("3.2. CustomAuthFailureHandler");
        String failMsg = "";

        if (exception instanceof AuthenticationServiceException) {
            failMsg = "로그인 정보가 일치하지 않습니다.";

        } else if (exception instanceof BadCredentialsException) {
            failMsg = "로그인 정보가 일치하지 않습니다.";

        } else if (exception instanceof LockedException) {
            failMsg = "로그인 정보가 일치하지 않습니다.";

        } else if (exception instanceof DisabledException) {
            failMsg = "로그인 정보가 일치하지 않습니다.";

        } else if (exception instanceof AccountExpiredException) {
            failMsg = "로그인 정보가 일치하지 않습니다.";

        } else if (exception instanceof CredentialsExpiredException) {
            failMsg = "로그인 정보가 일치하지 않습니다.";
        }

        // HashMap<String, Object> resultMap = new HashMap<>();
        // resultMap.put("userInfo", null);
        // resultMap.put("resultCode", 9999);
        // resultMap.put("failMsg", failMsg);

        // ObjectMapper objectMapper = new ObjectMapper();
        // PrintWriter printWriter = response.getWriter();
        // printWriter.print(objectMapper.writeValueAsString(resultMap));
        // printWriter.flush();
        // printWriter.close();
        // response.getOutputStream().println(objectMapper.writeValueAsString(resultMap));

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.sendRedirect("/login?error");
    }
}
