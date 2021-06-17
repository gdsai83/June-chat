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
        broadcastMessage(c.getUsername() + " вошел в чат");
    }

    public synchronized void unsubscribe(ClientHandler c) {
        clients.remove(c);
        broadcastMessage(c.getUsername() + " вышел из чата");
    }

    public boolean isNameSame(String name) {
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getUsername().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void broadcastMessage(String message) {
        for (ClientHandler c : clients) {
            c.sendMessage(message);
        }
    }
}
