import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

import javax.swing.*;

/**
 * <h1>SnatChatWindow</h1>
 * <p>Description here...</p>
 *
 * @author kevin
 */
public class SnatChatWindow extends JFrame implements SnatChatFrontend {
    private SnatChatRoom room;
    private Account account;

    private JTextField textField;
    private ChatMessagesComponent chatMessages;

    public SnatChatWindow(SnatChatRoom room, Account account) {
        this.room = room;
        this.account = account;

        init();
    }

    private void init() {
        setTitle(String.format("%s (%s)", account.getName(), room.getRoomName()));
        setLayout(new BorderLayout());
        JLabel username = new JLabel(account.getName(), JLabel.CENTER);
        username.setForeground(account.getColor());
        add(username, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        chatMessages = new ChatMessagesComponent();
        addLastMessages(chatMessages);
        centerPanel.add(chatMessages, BorderLayout.CENTER);

        JPanel stateGroupPanel = new JPanel();
        stateGroupPanel.setLayout(new GridBagLayout());
        ButtonGroup stateGroup = new ButtonGroup();
        for (State state : State.values()) {
            JRadioButton radioBtn = new JRadioButton(state.toString());
            radioBtn.setActionCommand(state.toString());
            radioBtn.addActionListener(e -> {
                account.setState(state);
                room.sendMessage("State of user '" + account.getName() + "' is now '" + state.toString() + "'");
            });
            stateGroup.add(radioBtn);
            stateGroupPanel.add(radioBtn);

            if (account.getState() == state) {
                radioBtn.setSelected(true);
            }

        }
        centerPanel.add(stateGroupPanel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        JPanel chatMessagePanel = new JPanel();
        chatMessagePanel.setLayout(new BoxLayout(chatMessagePanel, BoxLayout.X_AXIS));
        textField = new JTextField();
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (!(e.getKeyCode() == KeyEvent.VK_ENTER)) {
                    return;
                }
                sendMessage();
            }
        });

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> {
            sendMessage();
        });

        chatMessagePanel.add(textField);
        chatMessagePanel.add(sendButton);

        add(chatMessagePanel, BorderLayout.SOUTH);

        pack();
        setSize(640, 480);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                close();
            }
        });
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addLastMessages(ChatMessagesComponent chatMessages) {
        Stack<String> lines = new Stack<>();
        try (BufferedReader br = new BufferedReader(new FileReader(room.getRoomName() + ".txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.push(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int count = 0;
        for (String line : lines) {
            if (count > 10) {
                break;
            } else {
                receiveMessage(line);
                count++;
            }
        }
    }

    private void sendMessage() {
        if (textField.getText()
                     .isEmpty()) {
            JOptionPane.showMessageDialog(null,
                                          "Dear " + account.getName() + ", please enter a message",
                                          "Warning!",
                                          JOptionPane.WARNING_MESSAGE);
        } else {
            room.sendMessage(new Message(textField.getText(), account));
            textField.setText("");
        }
    }

    private void close() {
        room.unregister(this);
        room.sendMessage(account.getName() + " has left the room.");
    }

    @Override
    public void receiveMessage(Message msg) {
        JLabel text = new JLabel();
        text.setForeground(msg.getSender()
                              .getColor());
        startCountDown(text, msg.toString());
        chatMessages.add(text);
    }

    private void startCountDown(JLabel label, String text) {
        new Thread(() -> {
            int countdown = 30;

            while (countdown > 0) {
                label.setText(text + " [ " + countdown + " ]");
                countdown--;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            chatMessages.remove(label);
        }).start();
    }

    @Override
    public void receiveMessage(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.GRAY);
        startCountDown(label, text);
        chatMessages.add(label);
    }

    @Override
    public Account getAccount() {
        return null;
    }
}
