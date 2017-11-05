package Client;

import General.Field;
import General.GameInterface;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Евгений on 04.11.2017.
 */
public class StartClient {

    private static boolean MY_PLAYER = true;

    private static boolean checkwithregexp(String turn_){
        Pattern p = Pattern.compile("[0-9][a-j]");
        Matcher m = p.matcher(turn_);
        return m.matches();
    }
    private static boolean checkwithregexp2(String turn_){
        Pattern p = Pattern.compile("concede");
        Matcher m = p.matcher(turn_);
        return m.matches();
    }
    private static void print_field(){
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
    }

    public static void main(String[] args){

        String serviceName = "rmi://localhost/GameInterface";
        int port = Integer.parseInt("12345");
        GameInterface game1;
        try{
            Registry registry = LocateRegistry.getRegistry(port);
            game1 = (GameInterface) registry.lookup(serviceName);
            Field my_turn = new Field(0,0);
            Scanner in = new Scanner(System.in);
            String turn = "";
            print_field();
            while(true) {
                System.out.println("Введите ход:");
                do {
                    turn = in.nextLine();
                    if (!checkwithregexp(turn) && !checkwithregexp2(turn))
                        System.out.println("Неверный ход. Введите ход снова:");
                } while (!checkwithregexp(turn) && !checkwithregexp2(turn));
                if (checkwithregexp2(turn)) {
                    System.out.println(game1.concede(MY_PLAYER));
                } else {
                    my_turn.changeField(turn.charAt(0) - 48, turn.charAt(1) - 97);
                    //вызвать turn
                    System.out.println(game1.turn(my_turn,MY_PLAYER));
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
