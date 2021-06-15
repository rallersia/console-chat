package edu.school21.client.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class Client {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean available;

    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            available = true;
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            available = false;
        }
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }

    public String getMessage() {
        try {
            return in.readLine();
        } catch (SocketException e) {
            return (null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (null);
    }

    public void stopConnection() {
        try {
            available = false;
            sendMessage("exit");
            clientSocket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isAvailable() {
        return (available);
    }
}
