package org.lifemanager.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.lifemanager.backend.model.*;
import org.lifemanager.backend.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {
    private  UserService userService ;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request , HttpServletResponse response){

        return new ResponseEntity<>(userService.register(request , response) , HttpStatus.CREATED ) ;
    }
    @GetMapping("/refresh-token")
    public ResponseEntity<RefreshingResponse> refreshToken(@CookieValue(name="refreshToken" ,defaultValue = "NotFound") String cookie){
        RefreshingResponse  response = new RefreshingResponse(userService.refreshToken(cookie));
        return new ResponseEntity<>(response , HttpStatus.CREATED ) ;
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody VerifyUserRequest request){
        userService.verifyUser(request);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerification(@RequestParam String email){
        userService.resendVerificationCode(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
