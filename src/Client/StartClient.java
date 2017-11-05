package Client;

import General.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

/**
 * Created by Евгений on 04.11.2017.
 */
public class StartClient extends Start {

    private static boolean MY_PLAYER = true;

    public static void main(String[] args){

        System.out.println("Client Started");
        String serverServiceName = "rmi://localhost/GameInterface";
        String clientServiceName = "rmi://localhost/PrintingInterface";
        int port = 12345;
        int port2 = 12346;
        GameInterface game1;
        Printer printer;
        try{
            System.out.println("Connecting to server....");

            //RMI Server
            Registry server_registry = LocateRegistry.getRegistry(port);
            game1 = (GameInterface) server_registry.lookup(serverServiceName);
            System.out.println("Connected");
            game1.startGame();
            System.out.println("Printer Started");

            //RMI Client registration
            printer = new Printer();
            PrintingInterface printingInterface = (PrintingInterface) UnicastRemoteObject.exportObject(printer,0);
            Registry registry2 = LocateRegistry.createRegistry(port2);
            registry2.rebind(clientServiceName, printingInterface);

            Field my_turn = new Field(0,0);
            Scanner in = new Scanner(System.in);
            String turn = "";
            printDefaultGameField();
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
                }
            }
        } catch (Exception e){
            System.out.println("Client cannot connect to the server");
            e.printStackTrace();
        }

    }
}
