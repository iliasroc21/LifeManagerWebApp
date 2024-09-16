package org.lifemanager.backend.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.lifemanager.backend.entity.Token;
import org.lifemanager.backend.entity.User;
import org.lifemanager.backend.model.AuthRequest;
import org.lifemanager.backend.model.AuthResponse;
import org.lifemanager.backend.model.RegisterRequest;
import org.lifemanager.backend.model.VerifyUserRequest;
import org.springframework.stereotype.Service;



public interface UserService {
    User getUserByEmail(String email);

    AuthResponse register(RegisterRequest request , HttpServletResponse response);

    String refreshToken(String cookie);
    String verifyUser(VerifyUserRequest request);

    void resendVerificationCode(String email);
}
