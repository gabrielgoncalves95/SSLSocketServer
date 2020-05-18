package com.socket.server.init;

import com.socket.server.config.TCPServerConfig;
import com.socket.server.socket.TCPServer;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class TCPServerStarter implements ApplicationRunner {

    final TCPServerConfig tcpServerConfig;

    public TCPServerStarter(TCPServerConfig tcpServerConfig) {
        this.tcpServerConfig = tcpServerConfig;
    }

    @Override
    public void run(ApplicationArguments args) {
        new TCPServer(tcpServerConfig).init();
    }
}
