package ru.geekbrains.june.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private List<ClientHandler> clients;

    public Server() {
        try {
            this.clients = new ArrayList<>();
            ServerSocket serverSocket = new ServerSocket(8189);
            System.out.println("Сервер запущен. Ожидаем подключение клиентов..");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Подключился новый клиент");
                new ClientHandler(this, socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void subscribe(ClientHandler c) {
        clients.add(c);
        // Сделайте так, чтобы при выполнении этого метода сервер сообщал всем клиентам
        // что в чат зашел клиент (+его имя указывал)
    }

    public synchronized void unsubscribe(ClientHandler c) {
        clients.remove(c);
        // Сделайте так, чтобы при выполнении этого метода сервер сообщал всем клиентам
        // что из чата вышел клиент (+его имя указывал)
    }

    public synchronized void broadcastMessage(String message) {
        for (ClientHandler c : clients) {
            c.sendMessage(message);
        }
    }
}
