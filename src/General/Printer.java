package General;
import GUI.UI;
import General.Field;
import General.PrintingInterface;

import java.rmi.RemoteException;

/**
 * Created by Евгений on 05.11.2017.
 */
public class Printer implements PrintingInterface {
    private UI ui;

    public void setUi(UI ui) {
        this.ui = ui;
    }

    @Override
    public void printGamingField(int[] status) throws RemoteException {
        ui.changeIcon(new Field(status[0],status[1]),status[2]);
    }
}
