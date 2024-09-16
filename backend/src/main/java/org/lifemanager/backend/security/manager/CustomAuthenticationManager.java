package org.lifemanager.backend.security.manager;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.lifemanager.backend.entity.User;
import org.lifemanager.backend.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {
    private  PasswordEncoder passwordEncoder;
    private  UserService userService;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName() ;
        String password = authentication.getCredentials().toString();
        User user = userService.getUserByEmail(email);
        if(!passwordEncoder.matches(password , user.getPassword() )) {
            throw new BadCredentialsException("You provided an incorrect password");
        }
        return new UsernamePasswordAuthenticationToken(authentication.getName(), user.getPassword());
    }
}
