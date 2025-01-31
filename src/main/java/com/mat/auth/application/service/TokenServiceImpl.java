package com.mat.auth.application.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mat.auth.domain.port.in.TokenServicePort;
import com.mat.auth.domain.enums.UserRole;
import com.mat.auth.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenServicePort {
    private static final Logger logger = LoggerFactory.getLogger(TokenServiceImpl.class);

    @Value("${api.security.token.secret}")
    private String secret;

    @Override
    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret);
            String token = JWT.create()
                    .withIssuer("login-auth-ipa")
                    .withSubject(user.getUsername())
                    .withClaim("role", user.getRole().ordinal())
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
            logger.info("Token gerado para o usuário: {}", user.getUsername());
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao autenticar usuário!", exception);
        }
    }

    @Override
    public String validateToken(String token) {
        try {
            DecodedJWT decodedJWT = verifyToken(token);
            return decodedJWT.getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token inválido ou expirado", exception);
        }
    }

    @Override
    public boolean validateTokenForRole(String token, UserRole role) {
        try {
            DecodedJWT decodedJWT = verifyToken(token);
            if (isTokenExpired(decodedJWT)) {
                logger.info("Token expirado - {}", token);
                return false;
            }

            int roleClaim = decodedJWT.getClaim("role").asInt();
            if (roleClaim != role.ordinal()) {
                logger.info("Papel do token inválido: {}", roleClaim);
                return false;
            }
            return true;

        } catch (TokenExpiredException e) {
            logger.info("Token expirado: {}", e.getMessage());
            return false;
        } catch (JWTVerificationException e) {
            logger.error("Erro ao validar token: {}", e.getMessage());
            throw new RuntimeException("Token malformado ou inválido", e);
        } catch (Exception e) {
            logger.error("Erro inesperado ao validar token: {}", e.getMessage());
            throw new RuntimeException("Erro inesperado na validação do token", e);
        }
    }

    private DecodedJWT verifyToken(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.require(algorithm)
                .withIssuer("login-auth-ipa")
                .build()
                .verify(token);
    }

    private boolean isTokenExpired(DecodedJWT decodedJWT) {
        Date expiration = decodedJWT.getExpiresAt();
        return expiration != null && expiration.before(new Date());
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now()
                .plusHours(3)
                .toInstant(ZoneOffset.of("-03:00"));
    }
}
