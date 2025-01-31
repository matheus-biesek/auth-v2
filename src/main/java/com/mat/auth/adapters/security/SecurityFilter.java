package com.mat.auth.adapters.security;

import com.mat.auth.adapters.persistence.UserRepositoryAdapter;
import com.mat.auth.application.service.TokenServiceImpl;
import com.mat.auth.domain.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

    private final TokenServiceImpl tokenServiceImpl;
    private final UserRepositoryAdapter userRepositoryAdapter;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        if (token != null) {
            var username = tokenServiceImpl.validateToken(token);
            User user = userRepositoryAdapter.findByUsername(username).orElseThrow(() -> new RuntimeException("Usuário não encontrado - doFilterInternal"));
            if (user != null) {
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) {
            logger.info("Authorization do header está vazio!");
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }
}
