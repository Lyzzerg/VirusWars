package General;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Евгений on 03.11.2017.
 */
public interface GameInterface extends Remote, PrintingInterface {
    String turn(Field field,boolean current_player) throws RemoteException;
    String concede(boolean current_player) throws RemoteException;
    void startGame() throws RemoteException;
    boolean isGameEnded() throws RemoteException;
}
