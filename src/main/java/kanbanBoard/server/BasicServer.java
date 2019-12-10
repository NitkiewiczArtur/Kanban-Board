package kanbanBoard.server;

import kanbanBoard.server.server_Logic.ClientHandler;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class BasicServer {
    ArrayList outputStreams;

    public void send(String message) {
        Iterator it = outputStreams.iterator();
        while (it.hasNext()) {
            try {
                PrintWriter writer = (PrintWriter) it.next();
                writer.println(message);
                writer.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        outputStreams = new ArrayList();
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                outputStreams.add(writer);
                Thread thread = new Thread(new ClientHandler(clientSocket, this));
                thread.start();
                System.out.println("We have connection");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new BasicServer().start();
    }

}


