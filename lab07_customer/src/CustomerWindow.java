import newsRoom.data.NewsData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class CustomerWindow extends JFrame {
    private CustomerWindowListener listener;
    private JButton registerButton;
    private JButton unregisterButton;
    private JPanel newsPanel;
    private JLabel newsLabel;
    private JPanel customerNamePanel;
    private JTextField customerNameField;
    private boolean customerNameFilled;

    public CustomerWindow() {
        this.setTitle("CUSTOMER APP");

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBounds(0, 0, 600, 600);
        mainPanel.setBackground(new Color(0x144F62));

        setRegisterButton();
        setUnregisterButton();
        setNewsPanel();
        setNewsContentPanel();

        this.setResizable(false);
        this.setSize(600, 610);
        this.add(mainPanel);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.getContentPane().requestFocusInWindow();
    }

    private void setRegisterButton() {
        registerButton = new JButton("REGISTER");
        registerButton.setBounds(50, 25, 200, 50);
        registerButton.setEnabled(false);
        registerButton.addActionListener(e -> {
            if (e.getSource() == registerButton && listener != null) {
                if (!customerNameField.getText().equals("")) {
                    listener.registerCustomer(customerNameField.getText());
                }
            }
        });
        this.add(registerButton);
    }

    private void setUnregisterButton() {
        unregisterButton = new JButton("UNREGISTER");
        unregisterButton.setBounds(325, 25, 200, 50);
        unregisterButton.setEnabled(false);
        unregisterButton.addActionListener(e -> {
            if (e.getSource() == unregisterButton && listener != null) {
                listener.unregisterCustomer();
            }
        });
        this.add(unregisterButton);
    }

    private void setNewsPanel() {
        newsPanel = new JPanel();
        newsPanel.setBounds(50,175,475,340);
        newsPanel.setLayout(null);
        setNewsLabel();
        this.add(newsPanel);
    }

    private void setNewsLabel() {
        newsLabel = new JLabel();
        newsLabel.setVerticalAlignment(JLabel.TOP);
        newsLabel.setBounds(5,5,200,400);
        newsLabel.setFont(new Font("Arial", Font.PLAIN,18));
        newsPanel.add(newsLabel);
    }

    private void setNewsContentPanel() {
        customerNamePanel = new JPanel();
        customerNamePanel.setLayout(null);
        customerNamePanel.setBounds(50,100,475,50);
        setNewsContentField();
        this.add(customerNamePanel);
    }

    private void setNewsContentField() {
        customerNameField = new JTextField("CUSTOMER NAME");
        customerNameField.setBounds(5,5,465,40);
        customerNameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                customerNameField.setText("");
                customerNameFilled = true;
                enableButtons();
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (!customerNameField.getText().equals("")) {
                    customerNameField.setFocusable(false);
                }
            }
        });
        customerNamePanel.add(customerNameField);
    }

    private void enableButtons() {
        if (customerNameFilled) {
            registerButton.setEnabled(true);
            unregisterButton.setEnabled(true);
        }
    }

    public void setListener(CustomerWindowListener listener) {
        this.listener = listener;
    }

    public void updateNewsPanel(NewsData newsData) {
        if (newsData != null) {
            newsLabel.setText(
                "<html>" +
                    "<p>" + newsData.date + "</p>" +
                    "<p><br>" + newsData.news +"</p>" +
                "</html>"
            );
        } else {
            newsLabel.setText("");
        }
    }
}
