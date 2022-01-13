import newsRoom.data.NewsData;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CustomerWindow extends JFrame {
    private CustomerWindowListener listener;
    private JButton registerButton;
    private JButton unregisterButton;
    private JList<NewsData> newsJList;
    private DefaultListModel<NewsData> model;
    private JPanel newsPanel;
    private JLabel newsLabel;
    private List<NewsData> newsDataList;

    public CustomerWindow() {
        this.setTitle("CUSTOMER APP");

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBounds(0, 0, 600, 600);
        mainPanel.setBackground(new Color(0x144F62));

        setRegisterButton();
        setUnregisterButton();
        setNewsList();
        setNewsPanel();

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
        registerButton.addActionListener(e -> {
            if (e.getSource() == registerButton && listener != null) {
                listener.registerCustomer();
            }
        });
        this.add(registerButton);
    }

    private void setUnregisterButton() {
        unregisterButton = new JButton("UNREGISTER");
        unregisterButton.setBounds(325, 25, 200, 50);
        unregisterButton.addActionListener(e -> {
            if (e.getSource() == unregisterButton && listener != null) {
                listener.unregisterCustomer();
            }
        });
        this.add(unregisterButton);
    }

    private void setNewsList() {
        newsJList = new JList<>();
        model = new DefaultListModel<>();
        newsJList.setModel(model);
        newsJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        if (newsDataList != null) {
//            for (int i = 0; i < newsDataList.size(); i++) {
//                model.addElement("News: " + i);
//            }
            model.addAll(newsDataList);
        }
        newsJList.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()){
//                if (newsJList.getSelectedValue() != null) {
//                    int index = Integer.parseInt(
//                            newsJList.getSelectedValue().substring(newsJList.getSelectedValue().indexOf(" ") + 1));
//                    newsChosenFromList(newsDataList.get(index - 1));
//                }
                newsChosenFromList(newsJList.getSelectedValue());
            }
        });
        newsJList.setFont(new Font("Arial", Font.BOLD,20));
        newsJList.setBounds(50,125,200,400);
        this.add(newsJList);
    }

    private void newsChosenFromList(NewsData selectedValue) {
        if (selectedValue != null) {
            newsLabel.setText(
                "<html>" +
                    "<p>" + selectedValue.date + "</p>" +
                    "<p><br>" + selectedValue.news +"</p>" +
                "</html>"
            );
        } else {
            newsLabel.setText("");
        }
    }

    private void setNewsPanel() {
        newsPanel = new JPanel();
        newsPanel.setBounds(300,125,250,400);
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

    public void setListener(CustomerWindowListener listener) {
        this.listener = listener;
    }

    public void updateNewsList(List<NewsData> newsDataList) {
        this.newsDataList = newsDataList;
//        model.clear();
        if (!newsDataList.isEmpty()){
//            for (int i = 1; i < this.newsDataList.size() + 1; i++) {
//                model.addElement("News: " + i);
//            }
            model.addAll(this.newsDataList);
        }
        newsJList.updateUI();
    }
}
