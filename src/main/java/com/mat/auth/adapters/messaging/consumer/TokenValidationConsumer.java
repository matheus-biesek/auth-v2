package com.mat.auth.adapters.messaging.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mat.auth.application.service.TokenServiceImpl;
import com.mat.auth.domain.dto.request.TokenValidationRequestDTO;
import com.mat.auth.domain.port.in.TokenValidationConsumerPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenValidationConsumer implements TokenValidationConsumerPort {

    private final TokenServiceImpl tokenServiceImpl;
    private final ObjectMapper objectMapper;

    @Override
    public String validateTokenForRole(String jsonMessage) {
        try {
            TokenValidationRequestDTO request = this.objectMapper.readValue(jsonMessage, TokenValidationRequestDTO.class);

            boolean isValid = this.tokenServiceImpl.validateTokenForRole(request.getToken(), request.getRole());

            return isValid ? "VALID" : "INVALID";
        } catch (Exception e) {
            return "ERROR";
        }
    }
}
