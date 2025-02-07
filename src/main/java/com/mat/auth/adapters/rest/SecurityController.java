package com.mat.auth.adapters.rest;

import com.mat.auth.domain.dto.response.LoginResponseDTO;
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

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class SecurityController {

    private final SecurityServicePort securityService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(securityService.login(request.getUsername(), request.getPassword()));
    }






    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterUserRequestDTO request) {
        String token = securityService.registerUserWithRole(request.getUsername(), request.getPassword(), UserRole.USER);
        return ResponseEntity.status(HttpStatus.CREATED).body(new TokenResponseDTO(token));
    }

    @PatchMapping("/update-role")
    public ResponseEntity<?> updateRole(@Valid @RequestBody UpdateRoleRequestDTO request) {
        securityService.updateRoleUser(request.getUsername(), request.getRole());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@Valid @RequestBody UsernameRequestDTO request) {
        securityService.deleteUser(request.getUsername());
        return ResponseEntity.noContent().build();
    }
}
