package com.mat.auth.domain.port.in;

public interface TokenValidationConsumerPort {
    public String validateTokenForRole(String jsonMessage);
}
