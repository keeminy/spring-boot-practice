package com.example.demo.example.jwt;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.common.codes.ErrorCode;
import com.example.demo.config.exception.BusinessExceptionHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

    	// TODO : favicon.ico...h2 console....
//    	String passStr = "/h2-console";
//        List<String> passList = Arrays.asList(
//        		"/favicon.ico",
//        		"/api/v1/user/login",
//        		"/api/v1/test/generateToken",
//                "api/v1/code/codeList");
//        if (passList.contains(request.getRequestURI()) || request.getRequestURI().contains(passStr)) {
//        	filterChain.doFilter(request, response);
//        	return;
//        }

        List<String> allowList = Arrays.asList("/api/test");
        if (!allowList.contains(request.getRequestURI())) {
        	filterChain.doFilter(request, response);
        	return;
        }


        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader(AuthConstants.AUTH_HEADER);
        log.debug("header Check : " + header);

        try {
            if (header != null && !header.equalsIgnoreCase("")) {
                String token = TokenUtils.getTokenFromHeader(header);

                if (TokenUtils.isValidToken(token)) {
                    String userId = TokenUtils.getUserIdFromToken(token);
                    log.debug("UserId Check : %s", userId);

                    if (userId != null && userId.equalsIgnoreCase("")) {

                        // TODO : DB - Get User And Check Info
                        filterChain.doFilter(request, response);
                    } else {
                        throw new BusinessExceptionHandler("Token isn't userId",
                                ErrorCode.BUSINESS_EXCEPTION_ERROR);
                    }
                } else {
                    throw new BusinessExceptionHandler("Token is invalid",
                            ErrorCode.BUSINESS_EXCEPTION_ERROR);
                }
            } else {
                throw new BusinessExceptionHandler("Token is null",
                        ErrorCode.BUSINESS_EXCEPTION_ERROR);
            }
        } catch (Exception e) {

            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            String message = jsonResponseWrapper(e);
            log.error(message, e);

            // TODO : 응답값 RETURN
        }

    }


    private String jsonResponseWrapper(Exception e) throws JsonProcessingException {

        String resultMsg = "";
        // JWT 토큰 만료
        if (e instanceof ExpiredJwtException) {
            resultMsg = "TOKEN Expired";
        }
        // JWT 허용된 토큰이 아님
        else if (e instanceof SignatureException) {
            resultMsg = "TOKEN SignatureException Login";
        }
        // JWT 토큰내에서 오류 발생 시
        else if (e instanceof JwtException) {
            resultMsg = "TOKEN Parsing JwtException";
        }
        // 이외 JTW 토큰내에서 오류 발생
        else {
            resultMsg = "OTHER TOKEN ERROR";
        }

        ObjectMapper mapper = new ObjectMapper();

        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("status", 401);
        jsonMap.put("code", "9999");
        jsonMap.put("message", resultMsg);
        jsonMap.put("reason", e.getMessage());

        String jsonObject = mapper.writeValueAsString(jsonMap);
        logger.error(resultMsg, e);
        return jsonObject;
    }

}
