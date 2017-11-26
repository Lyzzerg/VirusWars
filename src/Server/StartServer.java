package Server;

import GUI.UI;
import General.*;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

/**
 * Created by Евгений on 21.10.2017.
 */
public class StartServer extends DefaultMethods {
    private static final boolean MY_PLAYER = false;
    private static Game game1;
    private static Scanner in;
    private static GameInterface gameInterface;
    private static Registry registry1, registry2;
    private static Printer serverPrinter;
    private static PrintingInterface toClientPrinter;
    private static PrintingInterface serverPrinterInterface;

    public static void main(String[] args) {
        Connection connection = new Connection();

        in = new Scanner(System.in);

        connection.ip_init(in);
        connection.port_init(in);
        connection.print_ports();

        System.out.println("Server Started");
        try {
            game1 = new Game();
            serverPrinter = new Printer();

            //RMI Server registration
            gameInterface = (GameInterface) UnicastRemoteObject.exportObject(game1, 0);
            serverPrinterInterface = (PrintingInterface) UnicastRemoteObject.exportObject(serverPrinter,0);

            registry1 = LocateRegistry.createRegistry(connection.getServerPort());
            registry1.rebind(connection.getServer_game_name(), gameInterface);
            registry1.rebind(connection.getServer_printing_name(), serverPrinter);

            System.out.println("Waiting Opponent");

            //RMI Client
            while(!game1.isGame_started()){
                Thread.sleep(1000);
            }
            registry2 = LocateRegistry.getRegistry(connection.getClientPort());
            toClientPrinter = (PrintingInterface) registry2.lookup(connection.getClient_printing_name());

            System.out.println("Opponent Founded");

            printDefaultGameField();
            UI ui = new UI(MY_PLAYER, game1, toClientPrinter);
            ui.setVisible(true);
            serverPrinter.setUi(ui);
            while(!game1.isGameEnded()) {
            }
        } catch (Exception e) {
            System.out.println("Server cannot create the game");
            e.printStackTrace();
        }
    }
}
