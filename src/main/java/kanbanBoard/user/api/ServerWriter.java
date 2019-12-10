package kanbanBoard.user.api;

import java.io.OutputStream;
import java.io.PrintWriter;

public class ServerWriter {
    private final PrintWriter writer;

    public ServerWriter(OutputStream outputStream) {
        this.writer = new PrintWriter(outputStream);
    }

    public void write(String text) {
        writer.println(text);
        writer.flush();
    }


}
