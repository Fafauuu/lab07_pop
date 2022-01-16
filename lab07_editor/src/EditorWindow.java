import newsRoom.data.CustomerData;
import newsRoom.data.NewsData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;

public class EditorWindow extends JFrame {
    private EditorWindowListener listener;
    private JButton removeNewsButton;
    private JButton sendNewsButton;
    private JButton updateCustomersButton;
    private JPanel newsContentPanel;
    private JTextArea newsContentTextArea;
    private JList<String> newsJList;
    private DefaultListModel<String> model;
    private String newsChosen;
    private JPanel newsPanel;
    private JTextArea newsTextArea;
    private List<NewsData> newsDataList;
    private JPanel customersPanel;
    private JLabel customersLabel;

    public EditorWindow() {
        this.setTitle("EDITOR APP");

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBounds(0, 0, 1200, 600);
        mainPanel.setBackground(new Color(0xBE9D16));

        setRemoveNewsButton();
        setSendNewsButton();
        setUpdateCustomersButton();
        setNewsContentPanel();
        setNewsList();
        setNewsPanel();
        setCustomersPanel();

        this.setResizable(false);
        this.setSize(1200, 610);
        this.add(mainPanel);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.getContentPane().requestFocusInWindow();
    }

    private void setRemoveNewsButton() {
        removeNewsButton = new JButton("REMOVE NEWS");
        removeNewsButton.setBounds(325, 25, 200, 50);
        removeNewsButton.addActionListener(e -> {
            if (e.getSource() == removeNewsButton && listener != null) {
                if (newsChosen != null && !newsChosen.equals("")){
                    listener.removeNews(newsChosen);
                }
            }
        });
        this.add(removeNewsButton);
    }

    private void setSendNewsButton() {
        sendNewsButton = new JButton("SEND NEWS");
        sendNewsButton.setBounds(625, 25, 200, 50);
        sendNewsButton.addActionListener(e -> {
            if (e.getSource() == sendNewsButton && listener != null) {
                listener.sendNews(newsContentTextArea.getText());
            }
        });
        this.add(sendNewsButton);
    }

    private void setUpdateCustomersButton() {
        updateCustomersButton = new JButton("UPDATE CUSTOMERS");
        updateCustomersButton.setBounds(925, 25, 200, 50);
        updateCustomersButton.addActionListener(e -> {
            if (e.getSource() == updateCustomersButton && listener != null) {
                listener.updateCustomers();
            }
        });
        this.add(updateCustomersButton);
    }

    private void setNewsContentPanel() {
        newsContentPanel = new JPanel();
        newsContentPanel.setLayout(null);
        newsContentPanel.setBounds(600,125,250,400);
        setNewsContentTextArea();
        this.add(newsContentPanel);
    }

    private void setNewsContentTextArea() {
        newsContentTextArea = new JTextArea("NEWS CONTENT");
        newsContentTextArea.setBounds(5,5,240,390);
        newsContentTextArea.setLineWrap(true);
        newsContentTextArea.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                newsContentTextArea.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {

            }
        });
        newsContentPanel.add(newsContentTextArea);
    }

    private void setNewsList() {
        newsJList = new JList<>();
        model = new DefaultListModel<>();
        newsJList.setModel(model);
        newsJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        if (newsDataList != null) {
            for (int i = 0; i < newsDataList.size(); i++) {
                model.addElement("News: " + i);
            }
        }
        newsJList.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()){
                if (newsJList.getSelectedValue() != null) {
                    int index = Integer.parseInt(
                            newsJList.getSelectedValue().substring(newsJList.getSelectedValue().indexOf(" ") + 1));
                    newsChosenFromList(newsDataList.get(index - 1));
                }
            }
        });
        newsJList.setFont(new Font("Arial", Font.BOLD,20));
        newsJList.setBounds(50,125,200,400);
        this.add(newsJList);
    }

    private void newsChosenFromList(NewsData selectedValue) {
        if (selectedValue != null) {
            newsTextArea.setText(selectedValue.news);
            newsChosen = selectedValue.news;
        } else {
            newsTextArea.setText("");
            newsChosen = "";
        }
    }

    public void updateNewsList(List<NewsData> newsDataList) {
        this.newsDataList = newsDataList;
        model.clear();
        if (!newsDataList.isEmpty()){
            for (int i = 1; i < this.newsDataList.size() + 1; i++) {
                model.addElement("News: " + i);
            }
        }
        newsJList.updateUI();
    }

    private void setNewsPanel() {
        newsPanel = new JPanel();
        newsPanel.setBounds(300,125,250,400);
        newsPanel.setLayout(null);
        setNewsLabel();
        this.add(newsPanel);
    }

    private void setNewsLabel() {
        newsTextArea = new JTextArea();
        newsTextArea.setLineWrap(true);
        newsTextArea.setWrapStyleWord(true);
        newsTextArea.setBounds(5,5,240,390);
        newsTextArea.setFont(new Font("Arial", Font.PLAIN,18));
        newsPanel.add(newsTextArea);
    }

    private void setCustomersPanel() {
        customersPanel = new JPanel();
        customersPanel.setBounds(900,125,250,400);
        customersPanel.setLayout(null);
        setCustomersLabel();
        this.add(customersPanel);
    }

    private void setCustomersLabel() {
        customersLabel = new JLabel();
        customersLabel.setVerticalAlignment(JLabel.TOP);
        customersLabel.setBounds(5,5,240,390);
        customersLabel.setFont(new Font("Arial", Font.PLAIN,18));
        customersPanel.add(customersLabel);
    }

    public void updateCustomersLabel(CustomerData[] customersData) {
        if (customersData != null && customersData.length > 0) {
            StringBuilder customersDataString = new StringBuilder();
            for (CustomerData customerData : customersData) {
                customersDataString.append("<p>");
                customersDataString.append(customerData.name);
                customersDataString.append("</p>");
            }
            customersLabel.setText(
                "<html>" +
                        customersDataString +
                "</html>"
            );
        } else {
            customersLabel.setText("");
        }
    }

    public void setListener(EditorWindowListener listener) {
        this.listener = listener;
    }
}
