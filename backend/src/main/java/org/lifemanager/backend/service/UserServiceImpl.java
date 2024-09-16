package org.lifemanager.backend.service;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.lifemanager.backend.entity.Token;
import org.lifemanager.backend.entity.TokenType;
import org.lifemanager.backend.entity.User;
import org.lifemanager.backend.exception.EntityAlreadyExistsException;
import org.lifemanager.backend.exception.EntityNotFoundException;
import org.lifemanager.backend.exception.RefreshTokenExpiredException;
import org.lifemanager.backend.exception.VerificationException;
import org.lifemanager.backend.model.AuthRequest;
import org.lifemanager.backend.model.AuthResponse;
import org.lifemanager.backend.model.RegisterRequest;
import org.lifemanager.backend.model.VerifyUserRequest;
import org.lifemanager.backend.repository.TokenRepository;
import org.lifemanager.backend.repository.UserRepository;
import org.lifemanager.backend.security.Constants;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements  UserService{
    private final UserRepository userRepository ;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    @Override
    public User getUserByEmail(String email) {
        return unwrappUser(userRepository.findByEmail(email) , email , User.class) ;
    }

    @Override
    public AuthResponse register(RegisterRequest request , HttpServletResponse response) {
        Optional<User> isUserExists = userRepository.findByEmail(request.getEmail());
        if(isUserExists.isPresent())throw new EntityAlreadyExistsException(request.getEmail() , User.class);
        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(false)
                .verificationCode(generateVerificationCode())
                .verificationCodeExpiresAt(LocalDateTime.now().plusMinutes(Constants.VERIFICATION_EXPIRATION))
                .build();
        sendVerificationEmail(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        Cookie cookie = new Cookie("refreshToken"  , refreshToken);
        cookie.setHttpOnly(true);  // Prevent JavaScript access
        cookie.setSecure(true);    // Send only over HTTPS (ensure
        cookie.setMaxAge((int)Constants.REFRESH_EXPIRATION/1000);
        response.addCookie(cookie);

        return AuthResponse.builder()
                .user(userRepository.save(user))
                .accessToken(jwtService.generateAccessToken(user))
                .build();
    }

    @Override
    public String refreshToken(String cookie) {
        if(cookie.equals("NotFound")) throw new  RefreshTokenExpiredException("No HttpOnly Cookie found");
        jwtService.decodeToken(cookie);
        String userEmail = jwtService.decodeToken(cookie).getSubject();
        User user = getUserByEmail(userEmail);
        return jwtService.generateAccessToken(user);
    }

    @Override
    public String verifyUser(VerifyUserRequest request) {
        User user = getUserByEmail(request.getEmail());
        if(user.getVerificationCodeExpiresAt() ==null || user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())){
            throw new VerificationException("Verification code has expired");
        }
        if(user.getVerificationCode().equals(request.getVerificationCode())){
            user.setEnabled(true);
            user.setVerificationCode(null);
            user.setVerificationCodeExpiresAt(null);
            userRepository.save(user);
        }
        else{
            throw new VerificationException("Invalid verification code");
        }
        return "Account Verified Successfully";
    }

    @Override
    public void resendVerificationCode(String email) {
        User user  = getUserByEmail(email);
        if(user.isEnabled()){
            throw  new VerificationException("The user with email : "+email+" is Already verified");
        }
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(Constants.RESEND_VERIFICATION_EXPIRATION));
        sendVerificationEmail(user);
        userRepository.save(user);
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
    private void sendVerificationEmail(User user) {
        String subject = "Account Verification";
        String verificationCode = "VERIFICATION CODE " + user.getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            // Handle email sending exception
            e.printStackTrace();
        }
    }
    private static <T> T unwrappUser(Optional<T> entity , String searchKey , Class<T> clazz){
        if(entity.isPresent())return entity.get();
        throw new EntityNotFoundException(searchKey  , clazz );
    }
}
