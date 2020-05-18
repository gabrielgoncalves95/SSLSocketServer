package com.socket.test.socket;

import com.socket.test.config.TCPServerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;

@Slf4j
public class TCPServer {

    @Autowired
    TCPServerConfig tcpServerConfig;

    final String KEY_STORE_NAME = "Test Socket";

    final String KEY_STORE_PASSWORD = "Test Socket";

    public TCPServer() {

        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream(tcpServerConfig.getServerSecurityCertFile()), KEY_STORE_PASSWORD.toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(keyStore, KEY_STORE_PASSWORD.toCharArray());

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            trustManagerFactory.init(keyStore);

            SSLContext ctx = SSLContext.getInstance("TLSv1.2");

            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            ctx.init(kmf.getKeyManagers(), trustManagers, null);

            SSLServerSocketFactory factory = ctx.getServerSocketFactory();
            ServerSocket listener = factory.createServerSocket(tcpServerConfig.getServerPort());
            SSLServerSocket sslListener = (SSLServerSocket) listener;
            sslListener.setNeedClientAuth(true);
            sslListener.setEnabledCipherSuites(sslListener.getSupportedCipherSuites());

            try {
                while (true) {
                    Socket socket = listener.accept();
                    log.info("New Client Connected");
                    new TCPServerSender(socket).start();
                }

            } catch (IOException error) {
                error.printStackTrace();
                log.error("Error during accept or thread!");
            }
        } catch (Exception ex) {
            log.error("Error during Socket Config!");
        }
    }

    @Bean
    public void init() {
        new TCPServer();
    }
}
