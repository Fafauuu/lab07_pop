import newsRoom.data.CustomerData;
import newsRoom.data.NewsData;
import newsRoom.interfaces.IConfiguration;
import newsRoom.interfaces.INotification;
import newsRoom.interfaces.IRegistration;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.Policy;
import java.util.*;

public class Broadcaster implements IConfiguration, IRegistration, BroadcasterWindowListener {
    private final List<CustomerData> customerDataList;
    private final List<NewsData> newsList;
    private final Map<Integer, NewsData> newsDataMap;
    private final BroadcasterWindow gui;

    public Broadcaster() {
        customerDataList = new ArrayList<>();
        newsList = new ArrayList<>();
        newsDataMap = new HashMap<>();
        gui = new BroadcasterWindow();
        gui.setListener(this);
    }

    public static void main(String[] args) {

        Policy.setPolicy(new MyPolicy());

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
            System.out.println("Security Manager set");
        }

        try {
            Registry reg = LocateRegistry.createRegistry(3000);
            reg.rebind("Server", UnicastRemoteObject.exportObject(new Broadcaster(), 0));
            System.out.println("Server is ready");
        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int addNews(String news) throws RemoteException {
        NewsData newsData = new NewsData();
        newsData.news = news;
        newsData.date = new Date();
        int newsDataId = newsDataMap.size();
        newsDataMap.put(newsDataId, newsData);
        newsList.add(newsData);
        return newsDataId;
    }

    @Override
    public boolean removeNews(int id) throws RemoteException {
        NewsData newsDataToRemove = newsDataMap.get(id);
        return newsList.remove(newsDataToRemove);
    }

    @Override
    public CustomerData[] getCustomers() throws RemoteException {
        CustomerData[] customerData = new CustomerData[customerDataList.size()];
        for (int i = 0; i < customerDataList.size(); i++) {
            customerData[i] = customerDataList.get(i);
        }
        return customerData;
    }

    @Override
    public boolean register(String name, INotification broadcast) throws RemoteException {
        CustomerData customerData = new CustomerData();
        customerData.name = name;
        customerData.broadcast = broadcast;
        customerDataList.add(customerData);
        System.out.println(name + " registered");
        return false;
    }

    @Override
    public boolean unregister(String name) throws RemoteException {
        customerDataList.removeIf(customerData -> customerData.name.equals(name));
        System.out.println(name + " unregistered");
        return false;
    }

    @Override
    public void updateNewsList() {
        Map<Integer, NewsData> mapToSend = new TreeMap<>();
        for (Integer id : newsDataMap.keySet()) {
            if (newsList.contains(newsDataMap.get(id))) {
                mapToSend.put(id, newsDataMap.get(id));
            }
        }
        gui.updateNewsList(mapToSend);
    }

    @Override
    public void editNews(Integer id, String news) {
        newsDataMap.get(id).news = news;
        System.out.println(newsDataMap.get(id).news);
    }

    @Override
    public void notifyCustomers(Integer id) {
        for (CustomerData customerData : customerDataList) {
            try {
                customerData.broadcast.notify(newsDataMap.get(id));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
