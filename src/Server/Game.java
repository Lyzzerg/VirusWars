package Server;

import General.Field;
import General.GameInterface;

import java.rmi.RemoteException;

/**
 * Created by Евгений on 21.10.2017.
 */
public class Game implements GameInterface{

    //Here's the Virus Wars Game Logic//

    //создание игры
    Game() throws RemoteException { // Конструктор
        playing_field = new PlayingField[10][10]; // Создаём поле 10 на 10
        for (int i=0; i<10; i++) {
            for(int j=0; j<10; j++){
                playing_field[i][j] = new PlayingField();
            }
        }
        player = FIRST_PLAYER; // Игрок начавший игру считается игроком делающим первый ход
        System.out.println(playing_field[2][0]);
        first_player_first_turn = true;
        second_player_first_turn = true;
        marked = new boolean [10][10];
        turn_number = 0;
        winner = 0;
    }

    //сделать ход
    @Override
    public String turn(Field field, boolean current_player) throws RemoteException{
        boolean you_can_turn = true;
        String result ="";
        if(winner==0) {
            if (is_my_turn(current_player)) { //если ход текущего игрока
                if (turn_number == 0) {
                    you_can_turn = can_turn();
                }
                if (you_can_turn) {
                    if (find(field)) {            //если у вас доступно 3 хода и ваш ход допустим
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
                        result= "Ход недопустим\n";
                    }
                    for (int i = 0; i < 10; i++) {
                        for (int j = 0; j < 10; j++) {
                            if (j == 0)
                                result = result + (i + " ");
                            result = result + ("|" +
                                    (playing_field[i][j].getCurrent_state() == X ? "X" :
                                            (playing_field[i][j].getCurrent_state() == O ? "O" :
                                                    (playing_field[i][j].getCurrent_state() == XDESTRUCTED ? "@" :
                                                            (playing_field[i][j].getCurrent_state() == ODESTRUCTED ? "*" : " ")))));
                        }
                        result = result + "|\n";
                    }
                    result = result + "   ";
                    for (int k = 0; k < 10; k++) {
                        result = result +((char) (97 + k) + " ");
                    }
                    result = result +"\n";
                } else {
                    winner = player ? 1 : 2; //если текущий игрок O то победили X иначе победили O
                    result = "Игра завершена. У вас нет допустимых ходов. \n Вы проиграли.";
                }
            } else {
                if (winner == 1 || winner == 2) {
                    result = "Игра завершена. У соперника нет допустимых ходов.\n Вы победили.";
                } else
                    result = "Сейчас не ваш ход";
            }
        } else{
            result = "Игра завершена. Победитель: " + (winner==1 ? "X" : "O");
        }
        return result;
    }

    //сдаться
    @Override
    public String concede(boolean current_player){
        String result = "Вы не можете сдаться не в свой ход";
        if(is_my_turn(current_player)) {
            winner = player ? 1 : 2;
            result = "Игра завершена.\nИгрок " + (player ? "O" : "X ") + "признал своё поражение.";
        }
        return result;
    }

    //проверка доступности хода
    private boolean find(Field field){

        //первые ходы для каждого игрока
        if(first_player_first_turn && (field.getNumeric_field()==9 && field.getWord_field() == 0)){
            first_player_first_turn = false;
            return true;
        } else if(second_player_first_turn && (field.getNumeric_field()==0 && field.getWord_field() == 9)){
            second_player_first_turn = false;
            return true;
        }

        int tempi, tempj;

        //ищем активные X и O
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i == 1 && j == 1) {} else { // проверяем все элементы кроме текущего (то есть всю окружность текущего)
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
        Field field1 = new Field(0,0);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i == 1 && j == 1){} else { // проверяем все элементы кроме текущего (то есть всю окружность текущего)
                    tempi = field.getNumeric_field() - 1 + i;
                    tempj = field.getWord_field() - 1 + j;
                    if (tempi < 10 && tempi > -1 && tempj < 10 && tempj > -1) { //если не вышли за пределы игрового поля
                        if ((playing_field[tempi][tempj].getCurrent_state() == //ищем первый мертвый вирус
                                (player ?  XDESTRUCTED : ODESTRUCTED)) &&
                                marked[tempi][tempj] == false) {
                            marked[tempi][tempj] = true;      //если найден маркируем его
                            field1.changeField(tempi,tempj);
                            if(find(field1)) //запускаем из него новый поиск и если нашли X или O
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
        int turns=0;

        if(player ? second_player_first_turn : first_player_first_turn) //можно ходить если это первый ход
            return true;

        for(int i=0; i<10; i++){
            for(int j=0; j<10; j++){  //смотрим по всему полю
                if(playing_field[i][j].getCurrent_state()==CLEAR ||
                        playing_field[i][j].getCurrent_state()==(player ? X : O)){ //можем ли мы найти 3 допустимых хода
                    if(find(new Field(i,j)))
                        turns++;
                }
            }
        }


        if(turns>=3){
            return true; //три допустимых хода найдено
        } else{
            return false; //не найдено трёх допустимых ходов
        }
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

    //Переменные//
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
    private PlayingField[][] playing_field; // Поле игры
    private boolean player; // Текущий игрок
    private boolean [][] marked;  //вспомогательное поле для поиска чейнов
    private int turn_number; // номер хода текущего игрока
    private int winner; //кто победитель
    private boolean first_player_first_turn;   //является ли данный ход первым для 1 игрока
    private boolean second_player_first_turn; //является ли данный ход первым для 2 игрока
}