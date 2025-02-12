package com.mat.auth.application.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mat.auth.adapters.rest.exception.TokenException;
import com.mat.auth.domain.port.in.TokenServicePort;
import com.mat.auth.domain.enums.UserRole;
import com.mat.auth.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenServicePort {

    @Value("${api.security.token.secret}")
    private String secret;

    @Override
    public String generateToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(this.secret);
        return JWT.create()
                .withIssuer("login-auth-api")
                .withSubject(user.getUsername())
                .withClaim("role", user.getRole().ordinal())
                .withExpiresAt(generateExpirationDate())
                .sign(algorithm);
    }

    @Override
    public String validateToken(String token) {
        DecodedJWT decodedJWT = verifyToken(token);
        return decodedJWT.getSubject();
    }

    private DecodedJWT verifyToken(String token) throws JWTVerificationException {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("login-auth-api")
                    .build()
                    .verify(token);
        } catch (TokenExpiredException e) {
            throw new TokenException("Token expirado.", e, HttpStatus.UNAUTHORIZED);
        } catch (JWTVerificationException e) {
            throw new TokenException("Token malformado ou inválido.", e, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            throw new TokenException("Erro inesperado ao verificar o token.", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean validateTokenForRole(String token, UserRole role) {

        DecodedJWT decodedJWT = verifyToken(token);

        int roleClaim;
        Claim claim = decodedJWT.getClaim("role");

        if (claim.isNull() || claim.asInt() == null) {
            throw new TokenException("Claim 'role' ausente ou inválida no token", HttpStatus.FORBIDDEN);
        }

        roleClaim = claim.asInt();
        if (roleClaim != role.ordinal()) {
            throw new TokenException("Papel do token inválido", HttpStatus.FORBIDDEN);
        }

        return true;
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now()
                .plusHours(3)
                .toInstant(ZoneOffset.of("-03:00"));
    }
}
