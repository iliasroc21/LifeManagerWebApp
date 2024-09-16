package org.lifemanager.backend.security.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.lifemanager.backend.entity.Token;
import org.lifemanager.backend.entity.User;
import org.lifemanager.backend.repository.TokenRepository;
import org.lifemanager.backend.security.Constants;
import org.lifemanager.backend.service.JwtService;
import org.lifemanager.backend.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if(header==null || !header.startsWith("Bearer")){
            filterChain.doFilter(request, response);
            return;
        }
        String token =header.replace("Bearer " , "");
        DecodedJWT decodedJWT = jwtService.decodeToken(token);
        String userEmail = decodedJWT.getSubject();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userEmail, null, Arrays.asList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
