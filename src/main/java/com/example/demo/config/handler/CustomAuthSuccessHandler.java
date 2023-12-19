package com.example.demo.config.handler;

import java.io.IOException;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class CustomAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {
        log.info("3.1. CustomLoginSuccessHandler ");

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        securityContextRepository.saveContext(context, request, response);

        // TODO: 추후 JWT 발급에 사용 할 예정
        // String token = TokenUtils.generateJwtToken(userVo);
        // jsonObject.put("token", token);
        // response.addHeader(AuthConstants.AUTH_HEADER, AuthConstants.TOKEN_TYPE + " "+
        // token);

        // response.setCharacterEncoding("UTF-8");
        // response.setContentType("application/json");
        // PrintWriter printWriter = response.getWriter();
        // printWriter.print("redirect:/main"); // 최종 저장된 '사용자 정보', '사이트 정보' Front 전달
        // printWriter.flush();
        // printWriter.close();

        getRedirectStrategy().sendRedirect(request, response, "/main");
    }
}
