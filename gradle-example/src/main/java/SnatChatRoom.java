import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * <h1>SnatChatRoom</h1>
 * <p>Description here...</p>
 *
 * @author kevin
 */
public class SnatChatRoom {
    private String roomName;
    private List<SnatChatFrontend> frontendList = new LinkedList<>();

    public SnatChatRoom(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void register(SnatChatFrontend s) {
        frontendList.add(s);
    }

    public void unregister(SnatChatFrontend s) {
        frontendList.remove(s);
    }

    public void sendMessage(Message msg) {
        frontendList.forEach(snatChatFrontend -> snatChatFrontend.receiveMessage(msg));
    }

    public void sendMessage(String text) {
        frontendList.forEach(snatChatFrontend -> snatChatFrontend.receiveMessage(text));
        logMessage(text);
    }

    private void logMessage(String text) {
        File logFile = new File(roomName + ".txt");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(logFile, true))) {
            bw.write(text);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
