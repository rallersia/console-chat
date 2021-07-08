package edu.school21.server.server;

import edu.school21.server.models.User;
import edu.school21.server.services.UsersService;
import edu.school21.server.services.operationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class Server {
    private ServerSocket serverSocket;
    private final UsersService service;
    private final List<EchoClientHandler> clients;


    @Autowired
    public Server(UsersService service) {
        this.service = service;
        clients = Collections.synchronizedList(new ArrayList<>());
    }


    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                EchoClientHandler clientHandler = new EchoClientHandler(serverSocket.accept(), service, clients);
                clients.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class EchoClientHandler extends Thread {
        private final Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private User user;
        private final UsersService service;
        private final List<EchoClientHandler> clients;

        public EchoClientHandler(Socket socket, UsersService service, List<EchoClientHandler> clients) {
            this.clientSocket = socket;
            this.service = service;
            this.clients = clients;
        }

        private boolean login() throws IOException {
            String command = in.readLine();
            if ("signUp".equals(command) || "signIn".equals(command)) {
                String username = in.readLine();
                String password = in.readLine();
                user = new User(null, username, password);
                if ("signUp".equals(command)) {
                    if (service.signUpUser(user) == operationStatus.SUCCESS) {
                        out.println(operationStatus.SUCCESS.toString());
                    } else {
                        out.println(operationStatus.FAIL.toString());
                        login();
                    }
                } else {
                    if (service.singInUser(user) == operationStatus.SUCCESS) {
                        out.println(operationStatus.SUCCESS.toString());
                    } else {
                        out.println(operationStatus.FAIL.toString());
                        login();
                    }
                }
                return true;
            }
            return false;
        }

        private void sendMessageToClients(String message) {
            for (EchoClientHandler client :
                    clients) {
                client.sendMessage(message);
            }
        }

        private void waitMessages() throws IOException {
            String message = in.readLine();
            while (!"exit".equals(message)) {
                sendMessageToClients(user.getUsername() + " > " + message);
                message = in.readLine();
                if (message == null) {
                    break;
                }
            }
            sendMessageToClients(user.getUsername() + " disconnected!");
        }

        public synchronized void sendMessage(String message) {
            out.println(message);
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                if (login()) {
                    sendMessageToClients(user.getUsername() + " connected!");
                    waitMessages();
                }
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
