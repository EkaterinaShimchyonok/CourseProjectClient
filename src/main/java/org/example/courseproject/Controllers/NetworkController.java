package org.example.courseproject.Controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
// SINGLETON
public class NetworkController {
    private static NetworkController instance;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private NetworkController() { }

    public static NetworkController getInstance() {
        if (instance == null) {
            instance = new NetworkController();
        }
        return instance;
    }

    public void connectToServer() {
        try {
            socket = new Socket("localhost", 5000);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("Не удалось подключиться к серверу: " + e.getMessage());
        }
    }

    public PrintWriter getOut() {
        return out;
    }

    public BufferedReader getIn() {
        return in;
    }

    public void closeConnection() {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Не удалось закрыть соединение: " + e.getMessage());
        }
    }
}
