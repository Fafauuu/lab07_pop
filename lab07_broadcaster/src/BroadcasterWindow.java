import newsRoom.data.NewsData;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class BroadcasterWindow extends JFrame {
    private BroadcasterWindowListener listener;
    private JButton updateNewsListButton;
    private JButton editNewsButton;
    private JButton saveNewsButton;
    private JButton notifyCustomersButton;
    private JList<String> newsJList;
    private DefaultListModel<String> model;
    private String newsChosen;
    private int idOfChosenNews;
    private JPanel newsPanel;
    private JTextArea newsTextArea;
    private JPanel newsContentPanel;
    private JTextArea newsContentTextArea;
    private List<NewsData> newsDataList;
    private Map<Integer, NewsData> newsDataMap;

    public BroadcasterWindow() {
        this.setTitle("BROADCASTER APP");

        newsDataMap = new TreeMap<>();
        newsDataList = new ArrayList<>();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBounds(0, 0, 1200, 600);
        mainPanel.setBackground(new Color(0x7C150A));

        setUpdateNewsListButton();
        setEditNewsButton();
        setSaveNewsButton();
        setNotifyCustomersButton();
        setNewsList();
        setNewsPanel();
        setNewsContentPanel();

        this.setResizable(false);
        this.setSize(1200, 610);
        this.add(mainPanel);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.getContentPane().requestFocusInWindow();
    }

    private void setUpdateNewsListButton() {
        updateNewsListButton = new JButton("UPDATE NEWS LIST");
        updateNewsListButton.setBounds(50, 25, 200, 50);
        updateNewsListButton.addActionListener(e -> {
            if (e.getSource() == updateNewsListButton && listener != null) {
                listener.updateNewsList();
            }
        });
        this.add(updateNewsListButton);
    }

    private void setEditNewsButton() {
        editNewsButton = new JButton("EDIT NEWS");
        editNewsButton.setBounds(325, 25, 200, 50);
        editNewsButton.addActionListener(e -> {
            if (e.getSource() == editNewsButton && listener != null) {
                saveNewsButton.setEnabled(true);
                newsContentTextArea.setText(newsChosen);
            }
        });
        this.add(editNewsButton);
    }

    private void setSaveNewsButton() {
        saveNewsButton = new JButton("SAVE NEWS");
        saveNewsButton.setBounds(625, 25, 200, 50);
        saveNewsButton.setEnabled(false);
        saveNewsButton.addActionListener(e -> {
            if (e.getSource() == saveNewsButton && listener != null && idOfChosenNews != -1) {
                String string = newsContentTextArea.getText();
                listener.editNews(idOfChosenNews, string);
                idOfChosenNews = -1;
                newsContentTextArea.setText("");
                saveNewsButton.setEnabled(false);
                listener.updateNewsList();
            }
        });
        this.add(saveNewsButton);
    }

    private void setNotifyCustomersButton() {
        notifyCustomersButton = new JButton("SEND TO CUSTOMERS");
        notifyCustomersButton.setBounds(925, 25, 200, 50);
        notifyCustomersButton.setEnabled(false);
        notifyCustomersButton.addActionListener(e -> {
            if (e.getSource() == notifyCustomersButton && listener != null) {
                if (newsChosen != null && !newsChosen.equals("")) {
                    listener.notifyCustomers(idOfChosenNews);
                }
            }
        });
        this.add(notifyCustomersButton);
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
            if (!e.getValueIsAdjusting()) {
                if (newsJList.getSelectedValue() != null) {
                    int index = Integer.parseInt(
                            newsJList.getSelectedValue().substring(newsJList.getSelectedValue().indexOf(" ") + 1));
                    newsChosenFromList(newsDataList.get(index - 1));
                    notifyCustomersButton.setEnabled(true);
                }
            }
        });
        newsJList.setFont(new Font("Arial", Font.BOLD, 20));
        newsJList.setBounds(50, 125, 200, 400);
        this.add(newsJList);
    }

    private void newsChosenFromList(NewsData selectedValue) {
        if (selectedValue != null) {
            for (Integer id : newsDataMap.keySet()) {
                if (newsDataMap.get(id).equals(selectedValue)) {
                    idOfChosenNews = id;
                }
            }
            newsTextArea.setText(selectedValue.news);
            newsChosen = selectedValue.news;
        } else {
            newsTextArea.setText("");
            newsContentTextArea.setText("");
        }
    }

    public void updateNewsList(Map<Integer, NewsData> newsDataMap) {
        this.newsDataMap = newsDataMap;
        newsDataList.clear();
        newsDataList.addAll(newsDataMap.values());
        model.clear();
        if (!newsDataList.isEmpty()) {
            for (int i = 1; i < this.newsDataList.size() + 1; i++) {
                model.addElement("News: " + i);
            }
        }
        newsJList.updateUI();
    }

    private void setNewsPanel() {
        newsPanel = new JPanel();
        newsPanel.setBounds(300, 125, 250, 400);
        newsPanel.setLayout(null);
        setNewsLabel();
        this.add(newsPanel);
    }

    private void setNewsLabel() {
        newsTextArea = new JTextArea();
        newsTextArea.setLineWrap(true);
        newsTextArea.setWrapStyleWord(true);
        newsTextArea.setBounds(5, 5, 240, 390);
        newsTextArea.setFont(new Font("Arial", Font.PLAIN, 18));
        newsPanel.add(newsTextArea);
    }

    private void setNewsContentPanel() {
        newsContentPanel = new JPanel();
        newsContentPanel.setLayout(null);
        newsContentPanel.setBounds(600, 125, 250, 400);
        setNewsContentTextArea();
        this.add(newsContentPanel);
    }

    private void setNewsContentTextArea() {
        newsContentTextArea = new JTextArea();
        newsContentTextArea.setBounds(5, 5, 240, 390);
        newsContentTextArea.setLineWrap(true);
        newsContentPanel.add(newsContentTextArea);
    }

    public void setListener(BroadcasterWindowListener listener) {
        this.listener = listener;
    }
}
