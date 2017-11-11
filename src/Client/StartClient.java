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

    private static boolean MY_PLAYER = true;

    public static void main(String[] args){

        System.out.println("Client Started");

        Scanner in = new Scanner(System.in);
        Connection connection = new Connection();
        GameInterface game1;
        Printer printer;

        connection.ip_init(in);
        connection.port_init(in);
        connection.print_ports();

        try{
            System.out.println("Connecting to server....");

            //RMI Server
            Registry server_registry = LocateRegistry.getRegistry(connection.getServerPort());
            game1 = (GameInterface) server_registry.lookup(connection.getServerServiceName());
            System.out.println("Connected");
            game1.startGame();
            System.out.println("Printer Started");

            //RMI Client registration
            printer = new Printer();
            PrintingInterface printingInterface = (PrintingInterface) UnicastRemoteObject.exportObject(printer,0);
            Registry registry2 = LocateRegistry.createRegistry(connection.getClientPort());
            registry2.rebind(connection.getClientServiceName(), printingInterface);

            Field my_turn = new Field(0,0);
            String turn = "";
            printDefaultGameField();
            UI ui = new UI(MY_PLAYER);
            ui.setVisible(true);
            while(!game1.isGameEnded()) {
                System.out.println("Введите ход:");
                do {
                    turn = in.nextLine();
                    if (!isTurnCorrect(turn) && !isConcede(turn))
                        System.out.println("Неверный ход. Введите ход снова:");
                } while (!isTurnCorrect(turn) && !isConcede(turn));
                if (isConcede(turn)) {
                    System.out.println(game1.concede(MY_PLAYER));
                    break; //конец игры если сдался
                } else {
                    my_turn.changeField(turn.charAt(0) - 48, turn.charAt(1) - 97);
                    System.out.println(game1.turn(my_turn,MY_PLAYER));
                    game1.printGamingField("");
                    ui.changeIcon(my_turn, game1.getFieldState(my_turn));
                }
            }
        } catch (Exception e){
            System.out.println("Client cannot connect to the server");
            e.printStackTrace();
        }

    }
}
