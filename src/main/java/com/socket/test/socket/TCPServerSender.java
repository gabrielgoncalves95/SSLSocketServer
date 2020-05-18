package com.socket.test.socket;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;

@Slf4j
public class TCPServerSender extends Thread {

    private Socket socket;
    private DataOutputStream dataOutputStream;

    public TCPServerSender(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String message = reader.readLine();
            byte[] messageConverted = message.getBytes();

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
