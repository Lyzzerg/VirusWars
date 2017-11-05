package General;

/**
 * Created by Евгений on 05.11.2017.
 */
public class PlayingField {

    private int current_state;

    public PlayingField() { current_state = -1; }

    public int getCurrent_state() { return current_state; }

    public void setCurrent_state(int current_state) {
        this.current_state = current_state;
    }
}
