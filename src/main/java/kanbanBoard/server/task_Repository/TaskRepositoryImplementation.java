package kanbanBoard.server.task_Repository;

import java.io.*;
import java.util.Random;

import static kanbanBoard.common.ServerRequests.CREATE_TASK;


public class TaskRepositoryImplementation implements TaskRepositoryInterface {


    @Override
    public boolean writeDataToFile(String message, Integer id) throws IOException {
        BufferedWriter saveToFileObject = null;
        try {
            saveToFileObject = new BufferedWriter(new FileWriter("baseData.txt", true));
            String task = message.split(":")[1];
            saveToFileObject.write(id + "," + task + "|");

            System.out.println("task written to file ");
        } catch (Exception e) {
            e.getStackTrace();
            //// ????? finally?
            return false;
        } finally {
            saveToFileObject.close();
            //try with resources lub ilioteka do zamykania?
            return true;
        }
    }

    @Override
    public boolean deleteDataFromFile(String s) {
        Random rd = new Random();

        String data = takeDataFromFile();
        String taskToDelete = s.split(":")[1].trim();
        String[] taskArray = data.split("\\|", 1000);
        for (int i = 0; i < taskArray.length -1 ; i++) {
            if(taskArray[i].contains(taskToDelete)){
                taskArray[i] = null;
                System.out.println(" TASK DELITED");
                if(i ==0 ){
                    deleteFile(new File("baseData.txt"));
                }
            } else {
                try {
                    if(i == 0){
                    deleteFile(new File("baseData.txt"));
                    }
                    String taskToWrite = taskArray[i].split(",")[1].trim();
                    System.out.println("deleted");

                    writeDataToFile(CREATE_TASK + taskToWrite, rd.nextInt() );
                    System.out.println("writting task nr: " + taskToWrite );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("File operation performed successfully");
       //czy zawsze wzwraca rue/?

        return true;

    }

    @Override
    public String takeDataFromFile() {
        BufferedReader dataTakerFromFile = null;
        try {
            dataTakerFromFile = new BufferedReader(new FileReader("baseData.txt" ));
            String fileContent = dataTakerFromFile.readLine();
            return fileContent;

        } catch (Exception e) {
            e.getStackTrace();
        }
        finally {
            try {
                dataTakerFromFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void deleteFile(File File){
        File file = new File("baseData.txt");
        file.delete();
    }
}
