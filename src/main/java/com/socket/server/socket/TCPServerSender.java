package com.socket.server.socket;

import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SSLSocket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

@Slf4j
public class TCPServerSender extends Thread {

    private final SSLSocket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    public TCPServerSender(SSLSocket socket) {
        this.socket = socket;
    }


    public void run() {
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

            byte[] buffer = new byte[4096];
            int length = dataInputStream.read(buffer);
            String received = "";
            if (length > -1) {
                received = new String(buffer, 0, length, StandardCharsets.UTF_8);
            }

            log.info("Received: " + received);
            String message = "Hello";
            byte[] messageConverted = message.getBytes(StandardCharsets.US_ASCII);

            log.info("Sending message!");
            dataOutputStream.write(messageConverted);
            socket.close();
            log.info("Client Disconnected!");

        } catch (IOException error) {
            log.error("Failed on socket communication!");
            error.printStackTrace();
        }
    }
}
