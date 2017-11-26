package General;

import java.util.Scanner;

/**
 * Created by Евгений on 05.11.2017.
 */
public class Connection extends DefaultMethods{
    private static final int DEFAULT_SERVER_PORT = 48654;
    private static final int DEFAULT_CLIENT_PORT = 49001;
    private static final String DEFAULT_GAME_NAME = "rmi://localhost/GameInterface";
    private static final String DEFAULT_PRINTING_NAME = "rmi://localhost/PrintingInterface";

    private String server_game_name = DEFAULT_GAME_NAME;
    private String server_printing_name = DEFAULT_PRINTING_NAME;
    private String client_printing_name = DEFAULT_PRINTING_NAME;

    int serverPort = DEFAULT_SERVER_PORT;
    int clientPort = DEFAULT_CLIENT_PORT;

    public String getClient_printing_name() {
        return client_printing_name;
    }

    public String getServer_game_name() {
        return server_game_name;
    }

    public String getServer_printing_name() {
        return server_printing_name;
    }

    public void changeServerIP(String IP) {
        this.server_game_name = "rmi://"+IP+"/GameInterface";
        this.server_printing_name = "rmi://"+IP+"/PrintingInterface";
    }

    public void changeClientIP(String IP) {
        this.client_printing_name = "rmi://"+IP+"/PrintingInterface";
    }

    public int getClientPort() {
        return clientPort;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public void setClientPort(int clientPort) {
        this.clientPort = clientPort;
    }

    public void port_init(Scanner in){

        System.out.println("Вы хотите изменить порты?");
        System.out.println("Yes/No");
        String command="";
        do {
            command = in.nextLine();
            if(!isYes(command)&&!isNo(command))
                System.out.println("повторите выбор");
        } while (!isYes(command)&&!isNo(command));

        if(isYes(command)) {
            String port;
            System.out.println("Введите порт на котором собираетесь запустить игру");
            System.out.println("Доступный диапазон портов:");
            System.out.println("48654—48999 49001—49150");
            do {
                port = in.nextLine();
                if (!isPort(port)) {
                    System.out.println("Неверный порт");
                }
            } while (!isPort(port));
            this.setServerPort(Integer.parseInt(port));
            System.out.println("Введите порт на который подключится клиент");
            System.out.println("Доступный диапазон портов:");
            System.out.println("48654—48999 49001—49150");
            do {
                port = in.nextLine();
                if (!isPort(port) || Integer.parseInt(port) == this.getServerPort()) {
                    System.out.println("Неверный порт");
                }
            } while (!isPort(port) || Integer.parseInt(port) == this.getServerPort());
            this.setClientPort(Integer.parseInt(port));
            System.out.println("Дождитесь подключения клиента");
        }
    }

    public void ip_init(Scanner in) {

        System.out.println("Вы хотите изменить IP?");
        System.out.println("Yes/No");
        String command = "";
        do {
            command = in.nextLine();
            if (!isYes(command) && !isNo(command))
                System.out.println("повторите выбор");
        } while (!isYes(command) && !isNo(command));
        if (isYes(command)) {
            String IP;
            System.out.println("Введите IP сервера");
            do {
                IP = in.nextLine();
                if (!isIP(IP)) {
                    System.out.println("Неверный IP");
                }
            } while (!isIP(IP));
            this.changeServerIP(IP);
            System.out.println("Введите IP клиента");
            do {
                IP = in.nextLine();
                if (!isIP(IP)) {
                    System.out.println("Неверный IP");
                }
            } while (!isIP(IP));
            this.changeClientIP(IP);
            System.out.println("Дождитесь подключения клиента");
        }
    }

    public void print_ports(){
        System.out.println("Выбранные порты:");
        System.out.println("Порт сервера: "+this.getServerPort());
        System.out.println("Порт клиента: "+this.getClientPort());
    }
}
