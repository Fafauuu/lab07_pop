import newsRoom.interfaces.IConfiguration;
import newsRoom.interfaces.INotification;
import newsRoom.interfaces.IRegistration;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Editor implements EditorWindowListener{
    private IConfiguration i;
    private final EditorWindow gui;

    public static void main(String[] args) {
        Editor editor = new Editor();
        editor.findServer();
    }

    public Editor() {
        gui = new EditorWindow();
        gui.setListener(this);
    }

    private void findServer() {
        try {
            Registry reg = LocateRegistry.getRegistry("localhost",3000);
            i = (IConfiguration) reg.lookup("Server");
            System.out.println("Server found");
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendNews(String news){
        try {
            int a =  i.addNews(news);
            System.out.println(a);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
