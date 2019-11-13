/**
 * <h1>SnatChatFrontend</h1>
 * <p>Description here...</p>
 *
 * @author kevin
 */
public interface SnatChatFrontend {
    void receiveMessage(Message msg);

    void receiveMessage(String text);

    Account getAccount();
}
