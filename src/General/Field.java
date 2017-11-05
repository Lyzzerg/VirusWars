package General;

import java.io.Serializable;

/**
 * Created by Евгений on 03.11.2017.
 */
public class Field implements Serializable{

    public Field (int numeric_field_, int word_field_){
        numeric_field = numeric_field_;
        word_field = word_field_;
    }

    public int getNumeric_field() {
        return numeric_field;
    }
    public int getWord_field(){
        return word_field;
    }
    public void changeField(int new_numeric, int new_word){
        numeric_field=new_numeric;
        word_field=new_word;
    }

    private int numeric_field;
    private int word_field;
}
