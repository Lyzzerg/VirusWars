package Client;
import General.PrintingInterface;

import java.rmi.RemoteException;

/**
 * Created by Евгений on 05.11.2017.
 */
public class Printer implements PrintingInterface {
    @Override
    public void printGamingField(String result) throws RemoteException {
        System.out.println(result);
        System.out.println("Введите ход");
    }
}
