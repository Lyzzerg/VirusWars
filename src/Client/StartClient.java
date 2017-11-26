package Client;

import GUI.UI;
import General.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

/**
 * Created by Евгений on 04.11.2017.
 */
public class StartClient extends DefaultMethods {
    private static Registry registry1, registry2;
    private static Printer clientPrinter;
    private static PrintingInterface clientPrinterInterface;
    private static PrintingInterface toServerPrinter;

    private static boolean MY_PLAYER = true;

    public static void main(String[] args){

        System.out.println("Client Started");

        Scanner in = new Scanner(System.in);
        clientPrinter = new Printer();
        Connection connection = new Connection();
        GameInterface game1;
        connection.ip_init(in);
        connection.port_init(in);
        connection.print_ports();

        try{
            System.out.println("Connecting to server....");

            //RMI Server
            registry1 = LocateRegistry.getRegistry(connection.getServerPort());
            game1 = (GameInterface) registry1.lookup(connection.getServer_game_name());
            toServerPrinter = (PrintingInterface) registry1.lookup(connection.getServer_printing_name());
            System.out.println("Connected");
            game1.startGame();
            System.out.println("Printer Started");

            //RMI Client registration
            clientPrinterInterface = (PrintingInterface) UnicastRemoteObject.exportObject(clientPrinter,0);
            registry2 = LocateRegistry.createRegistry(connection.getClientPort());
            registry2.rebind(connection.getClient_printing_name(), clientPrinterInterface);

            printDefaultGameField();
            UI ui = new UI(MY_PLAYER, game1, toServerPrinter);
            clientPrinter.setUi(ui);
            ui.setVisible(true);
            while(!game1.isGameEnded()) {
            }
        } catch (Exception e){
            System.out.println("Client cannot connect to the server");
            e.printStackTrace();
        }

    }
}
