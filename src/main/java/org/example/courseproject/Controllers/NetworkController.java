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

    public synchronized void connectToServer() {
        if (socket != null && socket.isConnected() && !socket.isClosed())
            return;

        try {
            socket = new Socket("localhost", 5000);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Установлено соединение с сервером localhost:5000");
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
                System.out.println("Закрытие потока ввода");
                in.close();
            }
            if (out != null) {
                System.out.println("Закрытие потока вывода");
                out.close();
            }
            if (socket != null) {
                System.out.println("Закрытие сокета");
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Не удалось закрыть соединение: " + e.getMessage());
        }
    }
}
