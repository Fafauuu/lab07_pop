import newsRoom.data.CustomerData;
import newsRoom.data.NewsData;
import newsRoom.interfaces.INotification;
import newsRoom.interfaces.IRegistration;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Customer extends CustomerData implements INotification, CustomerWindowListener {
    private IRegistration i;
    private final CustomerWindow gui;
    private boolean customerExported;
    private boolean nameSet;

    public static void main(String[] args) {
        new Customer();
    }

    public Customer() {
        gui = new CustomerWindow();
        gui.setListener(this);
    }

    @Override
    public void notify(NewsData newsdata) throws RemoteException {
        System.out.println("notified with: " + newsdata.news);
        gui.updateNewsPanel(newsdata);
    }

    @Override
    public void registerCustomer(String name) {
        try {
            Registry reg = LocateRegistry.getRegistry("localhost", 3000);
            i = (IRegistration) reg.lookup("Server");
            System.out.println("Server found");
            if (!nameSet) {
                this.name = name;
                nameSet = true;
            }
            if (!customerExported) {
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
