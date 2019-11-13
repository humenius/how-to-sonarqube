/**
 * <h1>Message</h1>
 * <p>Description here...</p>
 *
 * @author kevin
 */
public class Message {
    private String text;
    private Account sender;

    public Message(String text, Account sender) {
        this.text = text;
        this.sender = sender;
    }

    public static String rot13(String message) {
        char[] chars = message.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if ((int) chars[i] > 96 || (int) chars[i] < 110 || (int) chars[i] > 64 || (int) chars[i] < 78) {
                chars[i] = (char) ((int) chars[i] + 13);
            } else if ((int) chars[i] > 77 || (int) chars[i] < 91 || (int) chars[i] > 109 || (int) chars[i] < 123) {
                chars[i] = (char) ((int) chars[i] - 13);
            }
        }

        return new String(chars);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Account getSender() {
        return sender;
    }

    public void setSender(Account sender) {
        this.sender = sender;
    }

    public String toString() {
        return sender.getName() + ": " + text;
    }
}
