import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] ar) {
        int port = 6666; // случайный порт (может быть любое число от 1025 до 65535)
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

                if (line.equalsIgnoreCase(Commands.str_const_bye)) {
                    out.writeUTF("Пока мой дорогой друг!"); // отсылаем клиенту обратно ответ на запрос.
                    out.flush(); // заставляем поток закончить передачу данных.
                } else if (line.equalsIgnoreCase(Commands.str_const_hello)) { //если введена другая команда то вопрошаем
                    out.writeUTF("Привет друг!");
                    out.flush();
                }
                else if (line.equalsIgnoreCase(Commands.str_const_hay)) { //Сравниваем команду на сервере с приходящей командой
                    out.writeUTF("Как ты?"); // отсылаем клиенту обратно ответ на запрос.
                    out.flush(); // заставляем поток закончить передачу данных.
                } else if (line.equalsIgnoreCase(Commands.str_const_date)) { //если введена другая команда то вопрошаем
                    out.writeUTF(String.valueOf(java.util.Calendar.getInstance().getTime()));
                    out.flush();
                } else {
                    out.writeUTF("Что ты такое пишешь?"); // отсылаем клиенту обратно ответ на запрос.
                    out.flush(); // заставляем поток закончить передачу данных.
                }

                System.out.println("Waiting for the next line...");
                System.out.println();
            }
        } catch (Exception x) {
            System.out.println("Ух как не культурно!");
            System.out.println("Кто ж так вырубает??");
        }
    }
}