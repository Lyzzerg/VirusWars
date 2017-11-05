package Server;

import General.Field;
import General.GameInterface;
import General.Start;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

/**
 * Created by Евгений on 21.10.2017.
 */
public class StartServer extends Start{
    private static final boolean MY_PLAYER = false;

    public static void main(String[] args) {
        Game game1;
        System.out.println("Server Started");
        try {
            game1 = new Game();
            System.out.println("Game Created");

            //Регистрация в RMI
            String serviceName = "rmi://localhost/GameInterface";
            int port = 12345;
            GameInterface gameInterface = (GameInterface) UnicastRemoteObject.exportObject(game1, 0);
            Registry registry = LocateRegistry.createRegistry(port);
            registry.rebind(serviceName, gameInterface);

            System.out.println("Waiting Opponent");
            while(!game1.isGame_started()){
                Thread.sleep(1000);
            }
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
            }

        } catch (Exception e) {
            System.out.println("Server cannot create the game");
            e.printStackTrace();
        }
    }
}
