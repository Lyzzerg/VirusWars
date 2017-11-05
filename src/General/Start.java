package General;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Евгений on 05.11.2017.
 */
public class Start {
    public static boolean isTurnCorrect(String turn_){
        Pattern p = Pattern.compile("[0-9][a-j]");
        Matcher m = p.matcher(turn_);
        return m.matches();
    }
    public static boolean isConcede(String turn_){
        Pattern p = Pattern.compile("concede");
        Matcher m = p.matcher(turn_);
        return m.matches();
    }
}
