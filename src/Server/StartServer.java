package Server;

import General.Field;
import General.GameInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Евгений on 21.10.2017.
 */
public class StartServer {
    private static final boolean MY_PLAYER = false;

    private static boolean checkwithregexp1(String turn_){
        Pattern p = Pattern.compile("[0-9][a-j]");
        Matcher m = p.matcher(turn_);
        return m.matches();
    }
    private static boolean checkwithregexp2(String turn_){
        Pattern p = Pattern.compile("concede");
        Matcher m = p.matcher(turn_);
        return m.matches();
    }

    public static void main(String[] args) {
        Game game1;
        try {
            game1 = new Game();

            //Регистрация в RMI
            String serviceName = "rmi://localhost/GameInterface";
            int port = 12345;
            GameInterface gameInterface = (GameInterface) UnicastRemoteObject.exportObject(game1, 0);
            Registry registry = LocateRegistry.createRegistry(port);
            registry.rebind(serviceName, gameInterface);

            //Регистрация в RMI

            Field my_turn = new Field(0,0);
            Scanner in = new Scanner(System.in);
            String turn = "";
            for(int i=0; i<10; i++){
                for(int j=0; j<10; j++){
                    if(j==0)
                        System.out.print(i+" ");
                    System.out.print("| ");
                }
                System.out.println("|");
            }
            System.out.print("   ");
            for(int i=0; i<10; i++){
                System.out.print((char)(97+i) + " ");
            }
            System.out.println("");
            while(true) {
                System.out.println("Введите ход:");
                do {
                    turn = in.nextLine();
                    if (!checkwithregexp1(turn) && !checkwithregexp2(turn))
                        System.out.println("Неверный ход. Введите ход снова:");
                } while (!checkwithregexp1(turn) && !checkwithregexp2(turn));
                if(checkwithregexp2(turn)){
                    System.out.println(game1.concede(MY_PLAYER));
                    break; //конец игры если сдался
                }
                my_turn.changeField(turn.charAt(0) - 48, turn.charAt(1) - 97);
                System.out.println(game1.turn(my_turn, MY_PLAYER));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
