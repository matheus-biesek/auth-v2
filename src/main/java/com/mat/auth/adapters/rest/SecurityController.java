package com.mat.auth.adapters.rest;

import com.mat.auth.domain.dto.response.*;
import com.mat.auth.domain.port.in.SecurityServicePort;
import com.mat.auth.domain.dto.request.UserCredentialsRequestDTO;
import com.mat.auth.domain.dto.request.UpdateRoleRequestDTO;
import com.mat.auth.domain.dto.request.UsernameRequestDTO;
import com.mat.auth.domain.enums.UserRole;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class SecurityController {

    private final SecurityServicePort securityService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody UserCredentialsRequestDTO request) {
        TokenResponseDTO token  = securityService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponseDTO> register(@Valid @RequestBody UserCredentialsRequestDTO request) {
        TokenResponseDTO token = securityService.registerUserWithRole(request.getUsername(), request.getPassword(), UserRole.USER);
        return new ResponseEntity<>(token, HttpStatus.CREATED);
    }

    @PatchMapping("/update-role")
    public ResponseEntity<SuccessResponseDTO> updateRole(@Valid @RequestBody UpdateRoleRequestDTO request) {
        SuccessResponseDTO messageSuccess = securityService.updateRoleUser(request.getUsername(), request.getRole());
        return ResponseEntity.ok(messageSuccess);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<SuccessResponseDTO> deleteUser(@Valid @RequestBody UsernameRequestDTO request) {
        SuccessResponseDTO messageSuccess = securityService.deleteUser(request.getUsername());
        return ResponseEntity.ok(messageSuccess);
    }
}
