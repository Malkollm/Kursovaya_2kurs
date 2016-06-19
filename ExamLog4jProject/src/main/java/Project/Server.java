package Project;


import org.apache.log4j.LogManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Server extends User{
    // Подключаем запись логов к проекту, логи пишутся в файл и консоль
    static final org.apache.log4j.Logger rootLogger = LogManager.getRootLogger();
    static final org.apache.log4j.Logger userLogger = LogManager.getLogger(User.class);

    public Server(String strRandom) {
        super(strRandom);
    }


    public static void main(String[] args) throws Exception {
        Random random = new Random();

        //переопределили User.doSomething на ChildClass.doSomething
        User userClass = new ChildClass("");
        userClass.doSomething("Марсель Фаткуллин");

        //user - объект класса User
        //передаем от подкласса строку суперклассу
        User user = new User("Рандомное число запуска программы");
        System.out.println(user.strRandom);

        //записываем рандомное число [0,99] при каждом запуске сервера
        User us = new User(random.nextInt(99));
        System.out.println("Число " + us.randNumbLog);

        //запись инфо сообщения в лог файл с числом данного запуска сервера
        rootLogger.info("Запись в лог файл: " + user.strRandom + " " + us.randNumbLog);

       /* //Объект класса user
        User user = new User("Марсель", "Фаткуллин");
        System.out.println("Имя пользователя " + user.nameUs + " " + user.lastNameUs);*/


        /*//Выводим 2 сообщения уровня Инфо
        userLogger.info(user.showMeMessage());
        userLogger.info(user.giveMeASign());*/

        int port = 6666; // случайный порт (может быть любое число от 1025 до 65535)
        try {
            //использование оператора throw, для обработки исключения, проверяем порт на правильность диапазона
            if ((port < 1025) || (port > 65535)) throw new Exception("Порт не найден");

            ServerSocket ss = new ServerSocket(port); // создаем сокет сервера и привязываем его к вышеуказанному порту
            System.out.println("Waiting for a client...");

            Socket socket = ss.accept(); // заставляем сервер ждать подключений и выводим сообщение когда кто-то связался с сервером
            System.out.println("Got a client :)");
            System.out.println();

            // Берем входной и выходной потоки сокета, теперь можем получать и отсылать данные клиенту.
            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();

            // Конвертируем потоки в другой тип, чтоб легче обрабатывать текстовые сообщения.
            DataInputStream in = new DataInputStream(sin);
            DataOutputStream out = new DataOutputStream(sout);


            String line = null; //Строка необходима для принятия и передачи сообщения между клиентом и сервером
            while (true) { //Бесконечный цикл чтобы связь шла постоянно
                line = in.readUTF(); // ожидаем пока клиент пришлет строку текста.
                System.out.println("Client just sent me this line : " + line);
                System.out.println("I'm sending it back...");

                //Если строка сходится с командой на сервере то отсылаем обратно ответ на запрос
                if (line.equalsIgnoreCase(Commands.str_const_surgery)) {
                    out.writeUTF("1 корпус, 2 этаж, по правой стороне"); // отсылаем клиенту обратно ответ на запрос.
                    out.flush(); // заставляем поток закончить передачу данных.
                } else if (line.equalsIgnoreCase(Commands.str_const_maternity)) {
                    out.writeUTF("1 корпус, 2 этаж, по левой стороне");
                    out.flush();
                }
                else if (line.equalsIgnoreCase(Commands.str_const_oncology)) {
                    out.writeUTF("2 корпус, вход с улицы, 2 этаж");
                    out.flush();
                } else if (line.equalsIgnoreCase(Commands.str_const_cardiology)) {
                    out.writeUTF("1 корпус, 3 этаж");
                    out.flush();
                }
                else if (line.equalsIgnoreCase(Commands.str_const_endoscopic)) {
                    out.writeUTF("2 корпус, вход с улицы, 1 этаж");
                    out.flush();
                } else if (line.equalsIgnoreCase(Commands.str_const_Dental)) {
                    out.writeUTF("3 корпус, вход с улицы");
                    out.flush();
                }
                else if (line.equalsIgnoreCase(Commands.str_const_dep_intensive_care)) {
                    out.writeUTF("4 корпус, вход и въезд со стороны ворот");
                    out.flush();
                } else if (line.equalsIgnoreCase(Commands.str_const_nephrology)) {
                    out.writeUTF("1 корпус, 1 этаж, по коридору направо");
                    out.flush();
                }
                else if (line.equalsIgnoreCase(Commands.str_const_crisis)) {
                    out.writeUTF("1 корпус, 1 этаж, по коридору направо");
                    out.flush();
                } else if (line.equalsIgnoreCase(Commands.str_const_morgue)) {
                    out.writeUTF("2 корпус, -1 этаж");
                    out.flush();
                }
                else if (line.equalsIgnoreCase(Commands.str_const_restroom)) {
                    out.writeUTF("1 этаж, от лифта направо");
                    out.flush();
                } else if (line.equalsIgnoreCase(Commands.str_const_registry)) {
                    out.writeUTF("1 корпус, фойе");
                    out.flush();
                } else if (line.equalsIgnoreCase(Commands.str_const_date)) {
                    out.writeUTF(String.valueOf(java.util.Calendar.getInstance().getTime()));
                    out.flush();
                } else {
                    out.writeUTF("Запрос не найден");
                    out.flush();
                }

                System.out.println("Waiting for the next line...");
                System.out.println();
            }
        } catch (Exception ex) {
            userLogger.error("error message: " + ex.getMessage());
            userLogger.fatal("fatal error message: " + ex.getMessage());
            rootLogger.error("error message: " + ex.getMessage());
            rootLogger.fatal("fatal error message: " + ex.getMessage());
        }

        //debug
        if (rootLogger.isDebugEnabled()) { //если отладка включена
            rootLogger.debug("RootLogger: In debug message");
            userLogger.debug("UserLogger in debug");
        }
    }
}
