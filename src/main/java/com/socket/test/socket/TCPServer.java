package com.socket.test.socket;

import com.socket.test.config.TCPServerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;

@Slf4j
public class TCPServer {

    final TCPServerConfig tcpServerConfig;

    final String KEY_STORE_PASSWORD = "Password";

    @Autowired
    public TCPServer(TCPServerConfig tcpServerConfig) {
        this.tcpServerConfig = tcpServerConfig;

        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(loadCertificate().getInputStream(), KEY_STORE_PASSWORD.toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(keyStore, KEY_STORE_PASSWORD.toCharArray());

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            trustManagerFactory.init(keyStore);

            SSLContext ctx = SSLContext.getInstance("TLSv1.2");

            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            ctx.init(kmf.getKeyManagers(), trustManagers, null);

            SSLServerSocketFactory factory = ctx.getServerSocketFactory();
            ServerSocket listener = factory.createServerSocket(this.tcpServerConfig.getServerPort());
            SSLServerSocket sslListener = (SSLServerSocket) listener;
            sslListener.setNeedClientAuth(true);
            sslListener.setEnabledCipherSuites(sslListener.getSupportedCipherSuites());

            try {
                while (true) {
                    Socket socket = listener.accept();
                    log.info("New Client Connected");
                    new TCPServerSender(socket).start();
                }

            } catch (IOException ex) {
                ex.printStackTrace();
                log.error("Error during accept or thread! Error: " + ex.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Error during Socket Config! Error: " + ex.getMessage());
        }
    }

    @Bean
    public void init() {
        new TCPServer(tcpServerConfig);
    }

    public Resource loadCertificate() {
        return new ClassPathResource(tcpServerConfig.getServerSecurityCertFile());
    }
}
