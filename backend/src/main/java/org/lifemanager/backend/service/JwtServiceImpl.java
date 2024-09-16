package org.lifemanager.backend.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.RequiredArgsConstructor;
import org.lifemanager.backend.entity.Token;
import org.lifemanager.backend.entity.TokenType;
import org.lifemanager.backend.entity.User;
import org.lifemanager.backend.repository.TokenRepository;
import org.lifemanager.backend.security.Constants;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService{


    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(Constants.SECRET_KEY);
    }
    private JWTVerifier getVerifier() {
        return JWT.require(getAlgorithm()).build();
    }
    @Override
    public String generateAccessToken(User user) {
        return buildToken(user , Constants.JWT_EXPIRATION);
    }

    @Override
    public String generateRefreshToken(User user) {
        return buildToken(user, Constants.REFRESH_EXPIRATION);
    }

    @Override
    public DecodedJWT decodeToken(String token) {
        return getVerifier().verify(token);
    }

    private String buildToken(User user , Long expiration){
        return JWT.create()
                .withSubject(user.getEmail())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                .sign(getAlgorithm());
    }
}
