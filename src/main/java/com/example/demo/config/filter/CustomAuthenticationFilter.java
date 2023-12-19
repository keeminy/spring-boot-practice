package com.example.demo.config.filter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.example.demo.common.codes.ErrorCode;
import com.example.demo.config.exception.BusinessExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        UsernamePasswordAuthenticationToken authRequest;
        try {
            authRequest = getAuthRequest(request);
            setDetails(request, authRequest);

            return this.getAuthenticationManager().authenticate(authRequest);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(e.getMessage());
        } catch (Exception e) {
            throw new BusinessExceptionHandler(e.getMessage(), ErrorCode.BUSINESS_EXCEPTION_ERROR);
        }
    }

    private UsernamePasswordAuthenticationToken getAuthRequest(HttpServletRequest request) throws Exception {
        try {

            // ObjectMapper objectMapper = new ObjectMapper();
            // objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
            // User user = objectMapper.readValue(request.getInputStream(), User.class);

            log.debug("1.CustomAuthenticationFilter :: userId : " + request.getParameter("username") + " userPw : "
                    + request.getParameter("password"));

            return new UsernamePasswordAuthenticationToken(request.getParameter("username"),
                    request.getParameter("password"));
        } catch (UsernameNotFoundException ae) {
            throw new UsernameNotFoundException(ae.getMessage());
        } catch (Exception e) {
            throw new BusinessExceptionHandler(e.getMessage(), ErrorCode.IO_ERROR);
        }

    }

}
