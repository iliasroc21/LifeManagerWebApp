package org.lifemanager.backend.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.lifemanager.backend.entity.TokenType;
import org.lifemanager.backend.entity.User;
import org.lifemanager.backend.model.AuthRequest;
import org.lifemanager.backend.model.AuthResponse;
import org.lifemanager.backend.security.Constants;
import org.lifemanager.backend.security.manager.CustomAuthenticationManager;
import org.lifemanager.backend.service.JwtService;
import org.lifemanager.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@AllArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    private  CustomAuthenticationManager customAuthenticationManager;

    private  UserService userService;

    private  JwtService jwtService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            AuthRequest user = new ObjectMapper().readValue(request.getInputStream(), AuthRequest.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
            return customAuthenticationManager.authenticate(authentication);
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // Implementation here
        User user = userService.getUserByEmail(authResult.getName());
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        Cookie cookie = new Cookie("refreshToken"  , refreshToken);
        cookie.setHttpOnly(true);  // Prevent JavaScript access
        cookie.setSecure(true);    // Send only over HTTPS (ensure
        cookie.setMaxAge((int)Constants.REFRESH_EXPIRATION/1000);
        response.addCookie(cookie);
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        String jsonResponse = objectMapper.writeValueAsString(
                new AuthResponse(accessToken , user));
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(failed.getMessage());
        response.getWriter().flush();
    }
}
