package com.devcolibri.logpack;


import org.apache.log4j.LogManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {

    static final org.apache.log4j.Logger rootLogger = LogManager.getRootLogger();
    static final org.apache.log4j.Logger userLogger = LogManager.getLogger(User.class);

    public static void main(String[] args) throws UnknownHostException {

        User user = new User();
        user.setName("Marsel");
        user.setLastName("Fatkullin");
        userLogger.info(user.showMeMessage());
        userLogger.info(user.giveMeASign());

        int port = 6666;
        try {
            ServerSocket ss = new ServerSocket(port); // создаем сокет сервера и привязываем его к вышеуказанному порту
            System.out.println("Waiting for a client...");

            Socket socket = ss.accept(); // заставляем сервер ждать подключений и выводим сообщение когда кто-то связался с сервером
            System.out.println("Got a client :) ... Finally, someone saw me through all the cover!");
            System.out.println();

            // Берем входной и выходной потоки сокета, теперь можем получать и отсылать данные клиенту.
            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();

            // Конвертируем потоки в другой тип, чтоб легче обрабатывать текстовые сообщения.
            DataInputStream in = new DataInputStream(sin);
            DataOutputStream out = new DataOutputStream(sout);

            String line = null;
            while (true) {
                line = in.readUTF(); // ожидаем пока клиент пришлет строку текста.
                System.out.println("The dumb client just sent me this line : " + line);
                System.out.println("I'm sending it back...");
                out.writeUTF(line); // отсылаем клиенту обратно ту самую строку текста.
                out.flush(); // заставляем поток закончить передачу данных.
                System.out.println("Waiting for the next line...");
                System.out.println();
            }
        } catch (Exception ex) {
            userLogger.error("error message: " + ex.getMessage());
            userLogger.fatal("fatal error message: " + ex.getMessage());
            rootLogger.error("error message: " + ex.getMessage());
            rootLogger.fatal("fatal error message: " + ex.getMessage());
        }

       /* User user = new User();
        user.setName("Marsel");
        user.setLastName("Fatkullin");

        userLogger.info(user.showMeMessage());
        userLogger.info(user.giveMeASign());

        rootLogger.info("Root Logger: " + user.showMeMessage());

        //debug
        if (rootLogger.isDebugEnabled()) {
            rootLogger.debug("RootLogger: In debug message");
            userLogger.debug("UserLogger in debug");
        }

        try {
            User userNull = new User();
            userNull.getName().toString();
        } catch (NullPointerException ex) {
            userLogger.error("error message: " + ex.getMessage());
            userLogger.fatal("fatal error message: " + ex.getMessage());
        }*/
    }
}
