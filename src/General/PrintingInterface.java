package General;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Евгений on 05.11.2017.
 */
public interface PrintingInterface extends Remote {
    void printGamingField(String result) throws RemoteException;
}
