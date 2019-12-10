package kanbanBoard.common;

public class ServerResponses {
    public static String TASK_CREATED = "TASK_CREATED:";
    public static String ALL_TASKS = "ALL_TASKS:";
    public static String TASK_MOVED = "TASK_MOVED: ";
    public static String TASK_DELETED = "TASK_DELETED:";

    /*
    All Tasks := ALL_TASKS:{LIST_OF_TASKS}
    LIST_OF_TASKS := TASK1 | TASK2 | ...
    TASK := long, String

    Task Created := TASK_CREATED: long, String

     */


}
