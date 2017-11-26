package Client;

import GUI.UI;
import GameInterfaceApp.GameInterface;
import GameInterfaceApp.GameInterfaceHelper;
import General.*;
import PrintingInterfaceApp.PrintingInterface;
import PrintingInterfaceApp.PrintingInterfaceHelper;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;


/**
 * Created by Евгений on 04.11.2017.
 */
public class StartClient {
    private static Printer clientPrinter;
    private static ORB clientOrb;
    private static GameInterface game1;
    private static PrintingInterface toServerPrinter;

    private static boolean MY_PLAYER = true;

    public static void main(String[] args){

        System.out.println("Client Started");
        clientPrinter = new Printer();

        try{
            clientOrb = ORB.init(args, null);

            System.out.println("Connecting to server....");
            POA rootpoa = POAHelper.narrow(clientOrb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            rootpoa.activate_object(clientPrinter);
            PrintingInterface printref = PrintingInterfaceHelper.narrow(rootpoa.servant_to_reference(clientPrinter));

            // Получение корневого контекста именования
            org.omg.CORBA.Object clientObjRef =
                    clientOrb.resolve_initial_references("NameService");

            NamingContextExt ncClientRef = NamingContextExtHelper.narrow(clientObjRef);

            // Получение доступа к серверу по его имени
            String name = "GameInterface";
            game1 = GameInterfaceHelper.narrow(ncClientRef.resolve_str(name));

            name = "PrintingInterface";
            NameComponent path[] = ncClientRef.to_name( name );
            ncClientRef.rebind(path, printref);

            name = "ServerPrintingInterface";
            toServerPrinter = PrintingInterfaceHelper.narrow(ncClientRef.resolve_str(name));


            System.out.println("Получен доступ к объекту " + game1);

            UI ui = new UI(MY_PLAYER, game1, toServerPrinter);
            clientPrinter.setUi(ui);
            ui.setVisible(true);
            game1.startGame();
            clientOrb.run();
        } catch (Exception e){
            System.out.println("Client cannot connect to the server");
            e.printStackTrace();
        }

    }
}
