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

public class Broadcaster implements IConfiguration, IRegistration {
    private Map<String, INotification> customers;
    private List<NewsData> newsList;

    public Broadcaster() {
        customers = new HashMap<>();
        newsList = new ArrayList<>();
    }

    public static void main(String[] args) {

        Policy.setPolicy(new MyPolicy());

        if (System.getSecurityManager() == null){
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
        newsList.add(newsData);

        System.out.println(news);

        System.out.println(customers.size());

        for (INotification iNotification : customers.values()) {
            System.out.println("notified");
            iNotification.notify(newsData);
        }

        return newsList.indexOf(newsData);
    }

    @Override
    public boolean removeNews(int id) throws RemoteException {
        newsList.remove(id);
        return true;
    }

    @Override
    public CustomerData[] getCustomers() throws RemoteException {
        return new CustomerData[0];
    }

    @Override
    public boolean register(String name, INotification broadcast) throws RemoteException {
//        NewsData newsData = new NewsData();
//        newsData.date = new Date();
//        newsData.news = "just casual registration notifying";
//        broadcast.notify(newsData);
        customers.put(name, broadcast);
        System.out.println(name + " registered");
        System.out.println(customers.values());
        return false;
    }

    @Override
    public boolean unregister(String name) throws RemoteException {
        System.out.println(name + " unregistered");
        return false;
    }
}
