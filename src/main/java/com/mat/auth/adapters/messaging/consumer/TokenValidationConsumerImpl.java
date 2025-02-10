package com.mat.auth.adapters.messaging.consumer;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mat.auth.application.service.TokenServiceImpl;
import com.mat.auth.domain.dto.request.TokenValidationRequestDTO;
import com.mat.auth.domain.port.out.TokenValidationConsumerPort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.json.JsonParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class TokenValidationConsumerImpl implements TokenValidationConsumerPort {

    private static final Logger logger = LoggerFactory.getLogger(TokenValidationConsumerImpl.class);
    private final TokenServiceImpl tokenServiceImpl;
    private final ObjectMapper objectMapper;


    @Override
    @RabbitListener(queues = "token.validation")
    public String validateTokenForRole(String jsonMessage) {
        try {
            TokenValidationRequestDTO request = objectMapper.readValue(jsonMessage, TokenValidationRequestDTO.class);

            boolean isValid = tokenServiceImpl.validateTokenForRole(request.getToken(), request.getRole());
            return isValid ? "VALID" : "INVALID";

        } catch (JsonParseException e) {
            logger.error("Erro ao processar o JSON: ", e);

            return "Erro ao processar o JSON: " + e.getMessage();

        } catch (JsonMappingException e) {
            logger.error("Erro ao mapear o JSON: ", e);
            return "Erro ao mapear o JSON: " + e.getMessage();

        } catch (IOException e) {
            logger.error("Erro de I/O: ", e);
            return "Erro de I/O: " + e.getMessage();

        } catch (Exception e) {
            logger.error("Erro inesperado: ", e);
            return "Erro inesperado: " + e.getMessage();
        }
    }
}
