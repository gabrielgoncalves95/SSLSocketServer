package com.socket.test.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class TCPServerConfig {

    @Value("${TCPSocket.server.host}")
    private String serverHost;

    @Value("${TCPSocket.server.port}")
    private int serverPort;

    @Value("${TCPSocket.server.queue}")
    private int serverQueue;

    @Value("${TCPSocket.security.certFile}")
    private String serverSecurityCertFile;
}
