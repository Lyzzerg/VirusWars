package Server;

import General.*;
import java.util.LinkedList;
import java.util.List;
import GameInterfaceApp.*;
import org.omg.CORBA.*;
/**
 * Created by Евгений on 21.10.2017.
 */
public class Game extends GameInterfacePOA  {

    //Here's the Virus Wars Printer Logic//

    //создание игры
    Game() { // Конструктор
        playing_field = new PlayingField[10][10]; // Создаём поле 10 на 10
        for (int i=0; i<10; i++) {
            for(int j=0; j<10; j++){
                playing_field[i][j] = new PlayingField();
            }
        }
        player = FIRST_PLAYER; // Игрок начавший игру считается игроком делающим первый ход
        first_player_first_turn = true;
        second_player_first_turn = true;
        marked = new boolean [10][10];
        turn_number = 0;
        winner = 0;
        game_started = false;
        game_ended = false;
        current_turn = new Field(0,0);
        result = "";
    }

    //сделать ход
    public int[] turn(int n, int w, boolean current_player){
        current_turn = new Field(n,w);
        Field field = current_turn;
        boolean you_can_turn = true;
        String result_ = result;
        if(winner==0) {
            if (is_my_turn(current_player)) { //если ход текущего игрока
                if (turn_number == 0) {
                    you_can_turn = can_turn();
                }
                if (you_can_turn) {
                    clearMarked();
                    if (find(field) && !isAlreadyBusy()) {            //если у вас доступно 3 хода и ваш ход допустим
                        int state = playing_field[field.getNumeric_field()][field.getWord_field()].getCurrent_state();
                        switch (state) {
                            case CLEAR:
                                prolifiration(field);
                                break;
                            default:
                                destruction(field);
                                break;
                        }
                        if (turn_number == 2) {
                            turn_number = 0;
                            player = !player;
                        } else {
                            turn_number++;
                        }

                    } else {
                        result_= "You can not make such a move";
                    }
                } else {
                    winner = player ? 1 : 2; //если текущий игрок O то победили X иначе победили O
                    result_ = "The game is over. You do not have any valid moves. You lose.";
                    game_ended = true;
                }
            } else {
                if (winner == 1 || winner == 2) {
                    result_ = "The game is over. The opponent has no permissible moves. You won.";
                } else
                    result_ = "Now is not your move";
            }
        } else{
            result_ = "The game is over. Winner: " + (winner==1 ? "X" : "O");
        }
        result = result_;
        int[] status = new int[3];
        status[0] = n;
        status[1] = w;
        status[2] = playing_field[status[0]][status[1]].getCurrent_state();
        return status;
    }

    //сдаться
    @Override
    public String concede(boolean current_player){
        String result = "You can not surrender. It's not your move";
        if(is_my_turn(current_player)) {
            winner = player ? 1 : 2;
            result = "The game is over. Player " + (player ? "O" : "X ") + "acknowledged his defeat.";
        }
        game_ended = true;
        return result;
    }

    @Override
    public String getResult() {
        return result;
    }

    //начать игру
    @Override
    public void startGame(){
        game_started = true;
    }

    //проверка закончена ли игра
    @Override
    public boolean isGameEnded(){
        return game_ended;
    }

    //проверка начала игры
    public boolean isGame_started(){
        return game_started;
    }

    public void setOrb(ORB orb_val){
        orb=orb_val;
    }

    //проверка на первый ход
    private boolean isFirst_turn(){
        //первые ходы для каждого игрока
        if (first_player_first_turn && (current_turn.getNumeric_field() == 9 && current_turn.getWord_field() == 0)) {
            first_player_first_turn = false;
            return true;
        } else if (second_player_first_turn && (current_turn.getNumeric_field() == 0 && current_turn.getWord_field() == 9)) {
            second_player_first_turn = false;
            return true;
        } else {
            return false;
        }
    }

    //проверка доступности хода
    private boolean find(Field field) {

        if(isFirst_turn()){
            return true;
        }

        int tempi, tempj;

        //ищем активные X и O
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i == 1 && j == 1) {
                } else { // проверяем все элементы кроме текущего (то есть всю окружность текущего)
                    tempi = field.getNumeric_field() - 1 + i;
                    tempj = field.getWord_field() - 1 + j;
                    if (tempi < 10 && tempi > -1 && tempj < 10 && tempj > -1) { //если не вышли за пределы игрового поля
                        if (playing_field[tempi][tempj].getCurrent_state() == //ищем активный вирус
                                (player ? O : X)) {
                            return true; //если найден возвращаем true
                        }
                    }
                }
            }
        }
        //если мы не нашли активных X или O - ищем те убитые X или O, которых ещё нет в маркированном списке
        Field field1 = new Field(0, 0);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i == 1 && j == 1) {
                } else { // проверяем все элементы кроме текущего (то есть всю окружность текущего)
                    tempi = field.getNumeric_field() - 1 + i;
                    tempj = field.getWord_field() - 1 + j;
                    if (tempi < 10 && tempi > -1 && tempj < 10 && tempj > -1) { //если не вышли за пределы игрового поля
                        if ((playing_field[tempi][tempj].getCurrent_state() == //ищем первый мертвый вирус
                                (player ? XDESTRUCTED : ODESTRUCTED)) &&
                                marked[tempi][tempj] == false) {
                            marked[tempi][tempj] = true;      //если найден маркируем его
                            field1.changeField(tempi, tempj);
                            if (find(field1)) //запускаем из него новый поиск и если нашли X или O
                                return true;                // возвращаем true (всплытие из рекурсии)
                        }
                    }
                }
            }
        }
        return false; //не найдено ни одного живого вируса
    }

    //проверка времени хода
    private boolean is_my_turn(boolean current_player) {
        return player == current_player;
    }

    //проверка количества наличия 3 допустимых ходов
    private boolean can_turn(){
        List<Field> free_fields = new LinkedList<>();

        if(player ? second_player_first_turn : first_player_first_turn) //можно ходить если это первый ход
            return true;

        for(int i=0; i<10; i++){
            for(int j=0; j<10; j++){  //смотрим по всему полю
                if(playing_field[i][j].getCurrent_state()==CLEAR ||
                        playing_field[i][j].getCurrent_state()==(player ? X : O)){ //можем ли мы найти 3 допустимых хода
                    if(find(new Field(i,j)))
                        free_fields.add(new Field(i,j));
                }
            }
        }
        return find_three_steps(free_fields, free_fields.size());
    }

    private boolean find_three_steps(List<Field> free_fields, int steps){
        if (steps >= 3) {
            return true;
        } else if(steps == 0) {
            return false;
        }
        Field field = free_fields.get(free_fields.size()-1);
        free_fields.remove(free_fields.size()-1);
        for(int i=0; i<3; i++){
            for (int j=0;j<3; j++){
                int tempi = field.getNumeric_field()-1+i;
                int tempj = field.getWord_field()-1+j;
                if (tempi < 10 && tempi > -1 && tempj < 10 && tempj > -1) { //если не вышли за пределы игрового поля
                    if(playing_field[tempi][tempj].getCurrent_state() == CLEAR){
                        free_fields.add(new Field(tempi,tempj));
                    }
                }
            }
        }
        return find_three_steps(free_fields, free_fields.size());
    }

    private boolean isAlreadyBusy(){
        //нельзя ходить в уже занятые тобой клетки или убитые
        int check_state = playing_field[current_turn.getNumeric_field()][current_turn.getWord_field()].getCurrent_state();
        if (check_state == (player ? O : X) || check_state == XDESTRUCTED || check_state == ODESTRUCTED)
            return true;
        return false;
    }

    // Функция размножения
    private void prolifiration(Field field){
        playing_field[field.getNumeric_field()][field.getWord_field()].setCurrent_state(player ? O : X);
    }

    //Функция уничтожения
    private void destruction(Field field) {
        playing_field[field.getNumeric_field()][field.getWord_field()].setCurrent_state(player ? XDESTRUCTED :
                ODESTRUCTED);
    }

    //очистка вспомогательного поля для поиска
    private void clearMarked(){
        for(int i=0; i<10; i++){
            for(int j=0; j<10; j++){
                marked[i][j] = false;
            }
        }
    }

    //Переменные
    private class PlayingField {

        private int current_state;

        PlayingField() { current_state = CLEAR; }

        public int getCurrent_state() { return current_state; }

        public void setCurrent_state(int current_state) {
            this.current_state = current_state;
        }
    }

    private static final boolean FIRST_PLAYER = false;
    private static final int CLEAR = -1;
    private static final int X = 0;
    private static final int O = 1;
    private static final int XDESTRUCTED = 2;
    private static final int ODESTRUCTED = 3;
    private boolean game_started;
    private boolean game_ended;
    private PlayingField[][] playing_field; // Поле игры
    private Field current_turn;
    private boolean player; // Текущий игрок
    private boolean [][] marked;  //вспомогательное поле для поиска чейнов
    private int turn_number; // номер хода текущего игрока
    private int winner; //кто победитель
    private boolean first_player_first_turn;   //является ли данный ход первым для 1 игрока
    private boolean second_player_first_turn; //является ли данный ход первым для 2 игрока
    private String result;
    private ORB orb;
}