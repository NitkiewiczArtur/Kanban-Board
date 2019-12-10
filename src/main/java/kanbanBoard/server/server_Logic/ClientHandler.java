package kanbanBoard.server.server_Logic;

import kanbanBoard.server.BasicServer;
import kanbanBoard.server.task_Repository.TaskRepositoryInterface;
import kanbanBoard.server.BasicServer;
import kanbanBoard.server.task_Repository.TaskRepositoryImplementation;
import kanbanBoard.server.task_Repository.TaskRepositoryInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Random;

import static kanbanBoard.common.ServerRequests.CREATE_TASK;
import static kanbanBoard.common.ServerRequests.*;
import static kanbanBoard.common.ServerResponses.*;

public class ClientHandler implements Runnable {
    //prywatne
    BufferedReader reader;
    Socket socket;
    BasicServer server;

    Random random = new Random();

    public Integer id() {
        return random.nextInt();
    }
    public void setServer(BasicServer server) {
        this.server = server;
    }

    public ClientHandler(Socket clientSocket, BasicServer server) {
            setServer(server);
            socket = clientSocket;
        }


    @Override
    public void run() {

        setReader();

        String message;
        TaskRepositoryInterface fileHandler = new TaskRepositoryImplementation();
        try {
            while ((message = reader.readLine()) != null) {   
                if (message.contains(CREATE_TASK)) {
                    Integer id = id();
                    if (fileHandler.writeDataToFile(message, id)) {
                        server.send(TASK_CREATED + id + "," + message.split(":")[1]);
                    } else {
                        server.send("Task isn't created!!!!!");
                    }
                } else if (message.contains(GET_ALL_TASKS)) {
                        String dataFromFile = fileHandler.takeDataFromFile();
                        server.send(ALL_TASKS + dataFromFile);
                }else if(message.contains(MOVE_TASK)){
                    Integer id = id();
                    if (fileHandler.writeDataToFile(message, id)){
                        server.send(TASK_MOVED + id + "," + message.split(":")[1]);
                    }else{
                        server.send("Task NOT moved!");
                    }
                }else if(message.contains(DELETE_TASK)) {
                    if(fileHandler.deleteDataFromFile(message)){
                        server.send(TASK_DELETED + fileHandler.takeDataFromFile());
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setReader(){
        InputStreamReader isReader = null;
        try {
            isReader = new InputStreamReader(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        reader = new BufferedReader(isReader);
    }
}
