import newsRoom.data.NewsData;
import newsRoom.interfaces.IConfiguration;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Editor implements EditorWindowListener {
    private IConfiguration broadcaster;
    private final EditorWindow gui;
    private final List<NewsData> newsCreated;
    private final Map<Integer, String> newsCreatedMap;

    public static void main(String[] args) {
        Editor editor = new Editor();
        editor.findServer();
    }

    public Editor() {
        gui = new EditorWindow();
        gui.setListener(this);
        newsCreated = new ArrayList<>();
        newsCreatedMap = new HashMap<>();
    }

    private void findServer() {
        try {
            Registry reg = LocateRegistry.getRegistry("localhost", 3000);
            broadcaster = (IConfiguration) reg.lookup("Server");
            System.out.println("Server found");
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendNews(String news) {
        try {
            int newsID = broadcaster.addNews(news);
            NewsData newsData = new NewsData();
            newsData.news = news;
            newsCreatedMap.put(newsID, news);
            newsCreated.add(newsData);
            gui.updateNewsList(newsCreated);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeNews(String news) {
        for (Integer newsID : newsCreatedMap.keySet()) {
            if (newsCreatedMap.get(newsID).equals(news)) {
                boolean newsRemovedSuccessfully = false;
                try {
                    newsRemovedSuccessfully = broadcaster.removeNews(newsID);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                if (newsRemovedSuccessfully) {
                    newsCreated.removeIf(newsData -> newsData.news.equals(news));
                    gui.updateNewsList(newsCreated);
                }
            }
        }
    }

    @Override
    public void updateCustomers() {
        try {
            gui.updateCustomersLabel(broadcaster.getCustomers());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
