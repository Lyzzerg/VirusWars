package General;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Евгений on 05.11.2017.
 */
public class DefaultMethods {

    public static boolean isYes(String command){
        Pattern p = Pattern.compile("^[Yy][Ee][Ss]$");
        Matcher m = p.matcher(command);
        return m.matches();
    }

    public static boolean isNo(String command){
        Pattern p = Pattern.compile("^[Nn][Oo]$");
        Matcher m = p.matcher(command);
        return m.matches();
    }

    public static boolean isTurnCorrect(String turn_){
        Pattern p = Pattern.compile("^[0-9][a-j]$");
        Matcher m = p.matcher(turn_);
        return m.matches();
    }
    public static boolean isConcede(String turn_){
        Pattern p = Pattern.compile("concede");
        Matcher m = p.matcher(turn_);
        return m.matches();
    }

    public static boolean isPort(String command){
        Pattern p = Pattern.compile("^486([5-9][4-9])|(48[7-9][0-9][0-9])|4900[1-9]|490[1-9][0-9]|491[0-4][0-9]|49150");
        Matcher m = p.matcher(command);
        return m.matches();
    }

    public static boolean isIP(String command){
        Pattern p = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");
        Matcher m = p.matcher(command);
        return m.matches();
    }

    public static void printDefaultGameField(){
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
}
