package kanbanBoard.user.gui;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import kanbanBoard.user.api.ServerWriter;


import static kanbanBoard.common.ServerRequests.*;


public class MainBoardController {

    private ServerWriter serverWriter;
    @FXML
    private VBox toDoPane;
    @FXML
    private VBox doingPane;
    @FXML
    private VBox donePane;
    @FXML
    private TextField newTaskName;

    private TaskButton taskContainer;
    private Dragboard PaneDragBoard;
    private Dragboard taskDragBoard;


    public VBox getToDoPane() {
        return toDoPane;
    }

    public VBox getDoingPane() {
        return doingPane;
    }

    public VBox getDonePane() {
        return donePane;
    }
    private void moveTasktoToDo(Dragboard db) { serverWriter.write(MOVE_TASK + db.getString() + ".0"); }
    private void moveTasktoDoing(Dragboard db) {
        serverWriter.write(MOVE_TASK + db.getString() + ".1");
    }
    private void moveTasktoDone(Dragboard db) {
        serverWriter.write(MOVE_TASK + db.getString() + ".2");
    }

    private void deleteTaskFromToDo(Dragboard db){ serverWriter.write(DELETE_TASK + db.getString() + ".0"); }
    private void deleteTaskFromDoing(Dragboard db){
        serverWriter.write(DELETE_TASK + db.getString() + ".1");
    }
    private void deleteTaskFromDone(Dragboard db){ serverWriter.write(DELETE_TASK + db.getString() + ".2"); }

    void deleteTask(Parent parent){
        if (parent==toDoPane) {
            deleteTaskFromToDo(taskDragBoard);

        }
        else if(parent==doingPane){
            deleteTaskFromDoing(taskDragBoard);
        }
        else if(parent==donePane){
            deleteTaskFromDone(taskDragBoard);

        }
    }

    @FXML
    void createTask() {
        serverWriter.write(CREATE_TASK + newTaskName.getText() + ".0");
        newTaskName.clear();
    }

    @FXML
    public void initialize() {

        // Drag and drop handling for panes.
        toDoPane.setOnDragOver(event -> {
            if (event.getGestureSource() != doingPane &&
                    event.getDragboard().hasString()&&! toDoPane.getChildren().contains(taskContainer)) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });
        toDoPane.setOnDragDropped(event -> {
            PaneDragBoard = event.getDragboard();
            boolean success = false;
            if (PaneDragBoard.hasString()) {
                moveTasktoToDo(PaneDragBoard);
                PaneDragBoard = null;
                deleteTask(taskContainer.getParent());
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
        doingPane.setOnDragOver(event -> {
            if (event.getGestureSource() != doingPane &&
                    event.getDragboard().hasString()&& !doingPane.getChildren().contains(taskContainer)) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });
        doingPane.setOnDragDropped(event -> {
            PaneDragBoard = event.getDragboard();
            boolean success = false;
            if (PaneDragBoard.hasString() ) {
                moveTasktoDoing(PaneDragBoard);
                PaneDragBoard = null;
                deleteTask(taskContainer.getParent());
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();

        });
        donePane.setOnDragOver(event -> {
            if (event.getGestureSource() != donePane &&
                    event.getDragboard().hasString()&& !donePane.getChildren().contains(taskContainer)) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });
        donePane.setOnDragDropped(event -> {

                PaneDragBoard = event.getDragboard();
                boolean success = false;
                if (PaneDragBoard.hasString()) {
                    moveTasktoDone(PaneDragBoard);
                    PaneDragBoard = null;
                    deleteTask(taskContainer.getParent());
                    success = true;
                }

                event.setDropCompleted(success);
                event.consume();

        });

    }

    public void getAllTasks() {
        serverWriter.write(GET_ALL_TASKS);
    }

    public void setServerWriter(ServerWriter serverWriter) {
        this.serverWriter = serverWriter;
    }

    public void handleTask(String s) {
        Integer taskId = Integer.parseInt(s.split(",")[0].trim());
        String taskTxt = s.split(",")[1].split("\\.")[0];
        System.out.println(s.split("\\.")[1]);
        String taskStatusS  = s.split("\\.")[1];
        Integer taskStatus = Integer.parseInt(taskStatusS);
        TaskButton newTask = new TaskButton(taskId, taskTxt, taskStatus);
        if (taskStatus == 0) {
           createToDOTask(newTask);
        } else if (taskStatus == 1) {
            createDoingTask(newTask);
        } else if (taskStatus == 2) {
           createDoneTask(newTask);
        }
            newTask.setOnDragDetected(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    taskDragBoard = newTask.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent content = new ClipboardContent();
                    content.putString(newTask.getText());
                    taskDragBoard.setContent(content);
                    System.out.println("mouse dragged");
                    taskContainer = newTask;
                    event.consume();
                }
            });
        }

        public void handleAllTasks (String s){
            String[] tasksArray = s.split("\\|");
            for (String task : tasksArray) {
                handleTask(task);
                System.out.println("handled some task");
            }
        }
    private void createToDOTask(TaskButton newTask){
        newTask.setStyle("-fx-background-color:red; -fx-opacity: 0.8;");
        newTask.setPrefWidth(200);
        newTask.setPrefHeight(100);
        toDoPane.getChildren().add(newTask);
        System.out.println("handled task created(added)");
    }
    private void createDoingTask(TaskButton newTask){
        newTask.setStyle("-fx-background-color:yellow; -fx-opacity: 0.8;");
        newTask.setPrefWidth(200);
        newTask.setPrefHeight(100);
        doingPane.getChildren().add(newTask);
        System.out.println("handled move task to doing(added)");
        }
    private void createDoneTask(TaskButton newTask){
        newTask.setStyle("-fx-background-color:green; -fx-opacity: 0.8;");
        newTask.setPrefWidth(200);
        newTask.setPrefHeight(100);
        donePane.getChildren().add(newTask);
        System.out.println("handled move task to done (added)");
    }
}



