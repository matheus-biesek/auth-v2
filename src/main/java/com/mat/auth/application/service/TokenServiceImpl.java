package com.mat.auth.application.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mat.auth.adapters.rest.exception.TokenException;
import com.mat.auth.domain.port.in.TokenServicePort;
import com.mat.auth.domain.enums.UserRole;
import com.mat.auth.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
            return JWT.create()
                    .withIssuer("login-auth-ipa")
                    .withSubject(user.getUsername())
                    .withClaim("role", user.getRole().ordinal())
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
        } catch (Exception e) {
            throw new TokenException("Erro inesperado ao gerar token.", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public String validateToken(String token) {
        try {
            DecodedJWT decodedJWT = verifyToken(token);
            return decodedJWT.getSubject();
        } catch (TokenExpiredException e) {
            throw new TokenException("Token expirado", e, HttpStatus.UNAUTHORIZED);
        } catch (JWTVerificationException e) {
            throw new TokenException("Token malformado ou inválido", e, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            throw new TokenException("Erro inesperado ao validar o token", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean validateTokenForRole(String token, UserRole role) {
        try {
            DecodedJWT decodedJWT = verifyToken(token);

            if (isTokenExpired(decodedJWT)) {
                throw new TokenException("Token expirado", HttpStatus.UNAUTHORIZED);
            }

            int roleClaim = decodedJWT.getClaim("role").asInt();
            if (roleClaim != role.ordinal()) {
                throw new TokenException("Papel do token inválido", HttpStatus.FORBIDDEN);
            }

            return true;
        } catch (JWTVerificationException e) {
            throw new TokenException("Token malformado ou inválido", e, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            throw new TokenException("Erro inesperado ao validar do token pela role", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private DecodedJWT verifyToken(String token) throws JWTVerificationException {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("login-auth-ipa")
                    .build()
                    .verify(token);
        } catch (Exception e){
            throw new TokenException("Erro inesperado ao verificar token",e ,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean isTokenExpired(DecodedJWT decodedJWT) {
        try {
            Date expiration = decodedJWT.getExpiresAt();
            return expiration != null && expiration.before(new Date());
        } catch (Exception e) {
            throw new TokenException("Erro inesperado ao confirma se o token foi expirado",e ,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Instant generateExpirationDate() {
        try {
            return LocalDateTime.now()
                    .plusHours(3)
                    .toInstant(ZoneOffset.of("-03:00"));
        } catch (Exception e){
            throw new TokenException("Erro inesperado ao gerar data de expiração",e ,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
