package Client;

import org.omg.CORBA.ORB;

public class ORBThread extends Thread {
    ORB myOrb;

    // Конструктор класса
    ORBThread(ORB orb) {
        myOrb = orb;
    }

    // Метод запуска потока
    public void run() {
        myOrb.run();
    }
}
