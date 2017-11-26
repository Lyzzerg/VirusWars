package General;
import GUI.UI;
import PrintingInterfaceApp.PrintingInterfacePOA;

import java.rmi.RemoteException;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
import java.util.Properties;
/**
 * Created by Евгений on 05.11.2017.
 */
public class Printer extends PrintingInterfacePOA {
    private UI ui;
    private ORB orb;

    public void setOrb(ORB orb_val) {
        orb = orb_val;
    }

    public void setUi(UI ui) {
        this.ui = ui;
    }

    @Override
    public void printGamingField(int[] status) {
        ui.changeIcon(new Field(status[0],status[1]),status[2]);
    }
}
