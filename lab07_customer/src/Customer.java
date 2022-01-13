import newsRoom.data.CustomerData;
import newsRoom.data.NewsData;
import newsRoom.interfaces.INotification;
import newsRoom.interfaces.IRegistration;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Customer extends CustomerData implements INotification, CustomerWindowListener {
    private IRegistration i;
    private final CustomerWindow gui;
    private boolean customerExported;
    private final List<NewsData> newsDataList;

    public static void main(String[] args) {
        Customer customer = new Customer();
        customer.name = "secondCustomer";
    }

    public Customer() {
        gui = new CustomerWindow();
        gui.setListener(this);
        newsDataList = new ArrayList<>();
    }

    @Override
    public void notify(NewsData newsdata) throws RemoteException {
        System.out.println("notified with: " + newsdata.news);
        newsDataList.add(newsdata);
        gui.updateNewsList(newsDataList);
    }

    @Override
    public void registerCustomer() {
        try {
            Registry reg = LocateRegistry.getRegistry("localhost",3000);
            i = (IRegistration) reg.lookup("Server");
            System.out.println("Server found");
            if (!customerExported){
                this.broadcast = (INotification) UnicastRemoteObject.exportObject(this, 0);
                customerExported = true;
            }
            i.register(this.name, this.broadcast);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unregisterCustomer() {
        try {
            i.unregister(this.name);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
