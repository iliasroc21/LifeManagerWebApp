package org.lifemanager.backend.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.lifemanager.backend.entity.TokenType;
import org.lifemanager.backend.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Map;
import java.util.function.Function;
public interface JwtService {

    String generateAccessToken(User user);

    String generateRefreshToken(User user);

    DecodedJWT decodeToken(String token);
}
