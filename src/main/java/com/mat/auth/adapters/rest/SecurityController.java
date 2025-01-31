package com.mat.auth.adapters.rest;

import com.mat.auth.adapters.rest.exception.ValidationErrorHandler;
import com.mat.auth.domain.port.in.SecurityServicePort;
import com.mat.auth.domain.dto.request.LoginRequestDTO;
import com.mat.auth.domain.dto.request.RegisterUserRequestDTO;
import com.mat.auth.domain.dto.request.UpdateRoleRequestDTO;
import com.mat.auth.domain.dto.request.UsernameRequestDTO;
import com.mat.auth.domain.dto.response.TokenResponseDTO;
import com.mat.auth.domain.enums.UserRole;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class SecurityController {

    private final SecurityServicePort securityService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO request, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationErrorHandler.getErrorMessages(result));
        }
        return securityService.login(request.getUsername(), request.getPassword())
                .<ResponseEntity<?>>map(token -> ResponseEntity.ok(new TokenResponseDTO(token)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Usuário ou senha inválidos!")));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterUserRequestDTO request, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationErrorHandler.getErrorMessages(result));
        }
        String token = securityService.registerUserWithRole(request.getUsername(), request.getPassword(), UserRole.USER);
        return ResponseEntity.status(HttpStatus.CREATED).body(new TokenResponseDTO(token));
    }

    @PatchMapping("/update-role")
    public ResponseEntity<?> updateRole(@Valid @RequestBody UpdateRoleRequestDTO request, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationErrorHandler.getErrorMessages(result));
        }
        securityService.updateRoleUser(request.getUsername(), request.getRole());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@Valid @RequestBody UsernameRequestDTO request, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationErrorHandler.getErrorMessages(result));
        }
        securityService.deleteUser(request.getUsername());
        return ResponseEntity.noContent().build();
    }
}
