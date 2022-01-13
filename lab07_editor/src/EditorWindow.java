import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class EditorWindow extends JFrame {
    private EditorWindowListener listener;
    private JButton sendNewsButton;
    private JPanel newsContentPanel;
    private JTextField newsContentField;

    public EditorWindow() {
        this.setTitle("EDITOR APP");

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBounds(0, 0, 600, 600);
        mainPanel.setBackground(new Color(0x144F62));

        setSendNewsButton();
        setNewsContentPanel();

        this.setResizable(false);
        this.setSize(600, 610);
        this.add(mainPanel);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.getContentPane().requestFocusInWindow();
    }

    private void setSendNewsButton() {
        sendNewsButton = new JButton("REGISTER");
        sendNewsButton.setBounds(50, 25, 200, 50);
        sendNewsButton.addActionListener(e -> {
            if (e.getSource() == sendNewsButton && listener != null) {
                listener.sendNews(newsContentField.getText());
            }
        });
        this.add(sendNewsButton);
    }

    private void setNewsContentPanel() {
        newsContentPanel = new JPanel();
        newsContentPanel.setLayout(null);
        newsContentPanel.setBounds(50,150,500,400);
        setNewsContentField();
        this.add(newsContentPanel);
    }

    private void setNewsContentField() {
        newsContentField = new JTextField("NEWS CONTENT");
        newsContentField.setBounds(5,5,490,390);
        newsContentField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                newsContentField.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {

            }
        });
        newsContentPanel.add(newsContentField);
    }

    public void setListener(EditorWindowListener listener) {
        this.listener = listener;
    }
}
