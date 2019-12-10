package kanbanBoard.user.gui;


import javafx.scene.control.Button;

public class TaskButton extends Button {
    private final int id;
    public  int taskStatus;

    public TaskButton(int i, String s, int taskStatus) {
        super(s);
        this.id = i;
        this.taskStatus = taskStatus;
    }
}
