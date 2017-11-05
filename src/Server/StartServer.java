package Server;

import General.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

/**
 * Created by Евгений on 21.10.2017.
 */
public class StartServer extends DefaultMethods {
    private static final boolean MY_PLAYER = false;


    public static void main(String[] args) {
        Connection connection = new Connection();

        Scanner in = new Scanner(System.in);

        connection.ip_init(in);
        connection.port_init(in);
        connection.print_ports();

        Game game1;
        PrintingInterface toClientPrinter;
        System.out.println("Server Started");
        try {
            game1 = new Game();

            //RMI Server registration

            GameInterface gameInterface = (GameInterface) UnicastRemoteObject.exportObject(game1, 0);
            Registry registry1 = LocateRegistry.createRegistry(connection.getServerPort());
            registry1.rebind(connection.getServerServiceName(), gameInterface);

            System.out.println("Waiting Opponent");

            //RMI Client
            while(!game1.isGame_started()){
                Thread.sleep(1000);
            }
            Registry registry2 = LocateRegistry.getRegistry(connection.getClientPort());
            toClientPrinter = (PrintingInterface) registry2.lookup(connection.getClientServiceName());

            System.out.println("Opponent Founded");

            Field my_turn = new Field(0,0);
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
                toClientPrinter.printGamingField(game1.gamingFieldStatus());
            }

        } catch (Exception e) {
            System.out.println("Server cannot create the game");
            e.printStackTrace();
        }
    }
}
