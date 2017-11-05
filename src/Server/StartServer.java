package Server;

import Client.Printer;
import General.Field;
import General.GameInterface;
import General.PrintingInterface;
import General.Start;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

/**
 * Created by Евгений on 21.10.2017.
 */
public class StartServer extends Start {
    private static final boolean MY_PLAYER = false;

    public static void main(String[] args) {
        int port = 12345;
        int port2 = 12346;
        Game game1;
        PrintingInterface toClientPrinter;
        System.out.println("Server Started");
        try {
            game1 = new Game();

            //RMI Server registration
            String serverServiceName = "rmi://localhost/GameInterface";
            String clientServiceName = "rmi://localhost/PrintingInterface";

            GameInterface gameInterface = (GameInterface) UnicastRemoteObject.exportObject(game1, 0);
            Registry registry1 = LocateRegistry.createRegistry(port);
            registry1.rebind(serverServiceName, gameInterface);

            System.out.println("Waiting Opponent");

            //RMI Client
            while(!game1.isGame_started()){
                Thread.sleep(1000);
            }
            Registry registry2 = LocateRegistry.getRegistry(port2);
            toClientPrinter = (PrintingInterface) registry2.lookup(clientServiceName);

            System.out.println("Opponent Founded");

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
                if(isConcede(turn)){
                    System.out.println(game1.concede(MY_PLAYER));
                    break; //конец игры если сдался
                }
                my_turn.changeField(turn.charAt(0) - 48, turn.charAt(1) - 97);
                System.out.println(game1.turn(my_turn, MY_PLAYER));
                toClientPrinter.printGamingField(game1.printGamingField(new String()));
            }

        } catch (Exception e) {
            System.out.println("Server cannot create the game");
            e.printStackTrace();
        }
    }
}
