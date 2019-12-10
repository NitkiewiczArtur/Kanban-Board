package kanbanBoard;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import kanbanBoard.user.api.ServerReader;
import kanbanBoard.user.api.ServerWriter;
import kanbanBoard.user.gui.MainBoardController;

import java.io.IOException;
import java.net.Socket;

import static kanbanBoard.common.ServerRequests.PATH_TO_FXML;
import static kanbanBoard.common.ServerRequests.PORT;


public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        Socket socket = new Socket("localhost", PORT);
        ServerWriter serverWriter = new ServerWriter(socket.getOutputStream());
        fxmlAndServerHandle(PATH_TO_FXML, primaryStage, serverWriter, socket);
    }
    private void fxmlAndServerHandle(String fxmlPath,Stage primaryStage, ServerWriter serverWriter, Socket socket) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main_board.fxml"));
        Pane mainMenu = loader.load();
        MainBoardController controller = loader.getController();
        controller.setServerWriter(serverWriter);
        controller.getAllTasks();
        ServerReader serverReader = new ServerReader(controller, socket.getInputStream());
        new Thread(serverReader).start();
        Scene scene = new Scene(mainMenu);
        primaryStage.setScene(scene);
        primaryStage.setTitle("My Kanban Board");
        primaryStage.show();
    }
}
