package Server;

import GUI.UI;
import GameInterfaceApp.GameInterface;
import General.*;
import GameInterfaceApp.*;
import PrintingInterfaceApp.PrintingInterface;
import PrintingInterfaceApp.PrintingInterfaceHelper;
import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;

/**
 * Created by Евгений on 21.10.2017.
 */
public class StartServer {
    private static final boolean MY_PLAYER = false;
    private static Game game1;
    private static PrintingInterface toClientPrinter;
    private static Printer serverPrinter;
    private static ORB serverOrb;

    public static void main(String[] args) {

        System.out.println("Server Started");
        try {
            // Создаем и инициализируем экземпляр ORB
            serverOrb = ORB.init(args, null);

            // Получаем доступ к Root POA и активируем POAManager
            POA rootpoa = POAHelper.narrow(serverOrb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            // Создаем объект сервант и регистрируем в нем объект ORB
            game1 = new Game();
            game1.setOrb(serverOrb);
            serverPrinter = new Printer();
            serverPrinter.setOrb(serverOrb);


            org.omg.CORBA.Object refgame = rootpoa.servant_to_reference(game1);
            GameInterface gameref = GameInterfaceHelper.narrow(refgame);
            org.omg.CORBA.Object refprint = rootpoa.servant_to_reference(serverPrinter);
            PrintingInterface printref = PrintingInterfaceHelper.narrow(refprint);

            org.omg.CORBA.Object serverObjRef =
                    serverOrb.resolve_initial_references("NameService");

            NamingContextExt ncServerRef = NamingContextExtHelper.narrow(serverObjRef);

            // Связывание идентификатора "GameInterface" и объекта серванта
            String name = "GameInterface";
            NameComponent path[] = ncServerRef.to_name( name );
            ncServerRef.rebind(path, gameref);

            // Связывание идентификатора "ServerPrintingInterface" и объекта серванта
            name = "ServerPrintingInterface";
            path = ncServerRef.to_name( name );
            ncServerRef.rebind(path, printref);

            System.out.println("Server готов и ждет обращений ...");
            System.out.println("Waiting Opponent");

            // Ожидание обращений клиентов
            while(!game1.isGame_started()){
                Thread.sleep(1000);
            }
            System.out.println("Opponent Founded");

            name = "PrintingInterface";
            toClientPrinter = PrintingInterfaceHelper.narrow(ncServerRef.resolve_str(name));

            UI ui = new UI(MY_PLAYER, gameref, toClientPrinter);
            ui.setVisible(true);
            serverPrinter.setUi(ui);

            serverOrb.run();
        }
        catch (Exception e) {
            System.err.println("ОШИБКА: " + e);	// Выводим сообщение об ошибке
            e.printStackTrace(System.out);	// Выводим содержимое стека вызовов
        };

        System.out.println("GameServer работу завершил ...");

    }
}
