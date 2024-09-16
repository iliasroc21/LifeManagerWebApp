package org.lifemanager.backend.security.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.lifemanager.backend.exception.EntityNotFoundException;
import org.lifemanager.backend.model.AuthResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.filter.OncePerRequestFilter;
import  org.lifemanager.backend.exception.* ;

import java.io.IOException;

public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (EntityNotFoundException e) {
            handleException(response, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (JWTVerificationException e) {
            handleException(response, HttpServletResponse.SC_FORBIDDEN, "JWT NOT VALID");
        } catch (RuntimeException e) {
            handleException(response, HttpServletResponse.SC_BAD_REQUEST, "BAD REQUEST");
        }
    }

    private void handleException(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        String jsonResponse = objectMapper.writeValueAsString(new ErrorWrapper(message));
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}
