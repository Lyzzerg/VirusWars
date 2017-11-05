package Client;

import General.Field;
import General.GameInterface;
import General.Start;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

/**
 * Created by Евгений on 04.11.2017.
 */
public class StartClient extends Start{

    private static boolean MY_PLAYER = true;

    public static void main(String[] args){

        System.out.println("Client Started");
        String serviceName = "rmi://localhost/GameInterface";
        int port = Integer.parseInt("12345");
        GameInterface game1;
        try{
            System.out.println("Connecting to server....");

            //RMI
            Registry registry = LocateRegistry.getRegistry(port);
            game1 = (GameInterface) registry.lookup(serviceName);
            System.out.println("Connected");
            game1.startGame();
            System.out.println("Game Started");

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
