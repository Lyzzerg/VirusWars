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
    private static PrintingInterface toClientPrinter;

    public static int turn() throws RemoteException {
        String turn;
        Field my_turn = new Field(0,0);
        do {
            turn = in.nextLine();
            if (!isTurnCorrect(turn) && !isConcede(turn))
                System.out.println("Неверный ход. Введите ход снова:");
        } while (!isTurnCorrect(turn) && !isConcede(turn));
        if(isConcede(turn)){
            System.out.println(game1.concede(MY_PLAYER));
        }
        my_turn.changeField(turn.charAt(0) - 48, turn.charAt(1) - 97);
        game1.turn(my_turn, MY_PLAYER);
        toClientPrinter.printGamingField(game1.gamingFieldStatus());
        return game1.getFieldState(my_turn);
    }

    public static void main(String[] args) {
        Connection connection = new Connection();

        in = new Scanner(System.in);

        connection.ip_init(in);
        connection.port_init(in);
        connection.print_ports();

        System.out.println("Server Started");
        try {
            game1 = new Game();

            //RMI Server registration

            gameInterface = (GameInterface) UnicastRemoteObject.exportObject(game1, 0);
            registry1 = LocateRegistry.createRegistry(connection.getServerPort());
            registry1.rebind(connection.getServerServiceName(), gameInterface);

            System.out.println("Waiting Opponent");

            //RMI Client
            while(!game1.isGame_started()){
                Thread.sleep(1000);
            }
            registry2 = LocateRegistry.getRegistry(connection.getClientPort());
            toClientPrinter = (PrintingInterface) registry2.lookup(connection.getClientServiceName());

            System.out.println("Opponent Founded");

            printDefaultGameField();
            UI ui = new UI(MY_PLAYER, game1);
            ui.setVisible(true);
            while(!game1.isGameEnded()) {
            }

        } catch (Exception e) {
            System.out.println("Server cannot create the game");
            e.printStackTrace();
        }
    }
}
