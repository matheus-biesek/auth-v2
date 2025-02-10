package com.mat.auth.domain.port.out;

public interface TokenValidationConsumerPort {
    public String validateTokenForRole(String jsonMessage);
}
