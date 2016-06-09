package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Main extends Application { // JavaFX приложения наследуют класс javafx.application.Application

    @Override
    public void start(final Stage primaryStage) throws Exception {
        int serverPort = 6666; // здесь обязательно нужно указать порт к которому привязывается сервер.
        String address = "127.0.0.1"; // это IP-адрес компьютера, где исполняется наша серверная программа.

        try {
            InetAddress ipAddress = InetAddress.getByName(address); // создаем объект который отображает вышеописанный IP-адрес.
            System.out.println("Any of you heard of a socket with IP address " + address + " and port " + serverPort + "?");
            Socket socket = new Socket(ipAddress, serverPort); // создаем сокет используя IP-адрес и порт сервера.
            System.out.println("Yes! I just got hold of the program.");

            // Берем входной и выходной потоки сокета, теперь можем получать и отсылать данные клиентом.
            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();

            // Конвертируем потоки в другой тип, чтоб легче обрабатывать текстовые сообщения.
            final DataInputStream in = new DataInputStream(sin);
            final DataOutputStream out = new DataOutputStream(sout);


            final GridPane grid = new GridPane();
            // Stage - это контейнер, ассоциированный с окном

            // Если вы загляните в файл sample.fxml, то у видете в нем XML объявление элемента GridPane, т.е. табличного контейнера
            // Этот контейнер мы будем считать корневым, т.е. все элементы нашего приложения будут содержаться в нем
            final Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

            primaryStage.setTitle("Client"); // задаем заголовок окна

            // создаем сцену с заданными шириной и высотой и содержащую наш корневым контейнером, и связываем ее с окном
            primaryStage.setScene(new Scene(root, 450, 300));

            // гибкая сетка из столбцов и строк
            grid.setAlignment(Pos.CENTER); // сетка в центре
            grid.setHgap(10); //пробелы между строками и столбцами
            grid.setVgap(10);
            grid.setPadding(new Insets(30, 30, 30, 30)); //пространство вокруг сетки по 25 пикселей

            // сцена шириной 300 и высотой 275
            Scene scene = new Scene(grid, 400, 375);
            primaryStage.setScene(scene);

            // объект текст который нельзя изменить
            Text sceneTitle = new Text("Команды");
            // задание шрифта/размера/стиля
            sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
            // добавляет объект к сцене
            grid.add(sceneTitle, 0, 0, 2, 1);

            //метка
            Label userName = new Label("Команда:");
            grid.add(userName, 0, 1);

            // текстовое поле для ввода команд
           // line = keyboard.readLine();
            final TextField userTextField = new TextField();
            grid.add(userTextField, 1, 1);


            // метка
            Label pw = new Label("Ответ:");
            grid.add(pw, 0, 2);

            // текстовое поле для вывода результата запроса
            final TextField resultTextField = new TextField();
            grid.add(resultTextField, 1, 2);


            ////////////////////////////////
            /// КНОПКИ      ///
            ///////////////////////////////

            // метка
            Label lSend = new Label("Поиск на сервере:");
            grid.add(lSend, 0, 4);

            // кнопка проверки команды у сервера
            Button bSend = new Button("Проверить");
            // вспомогательный объект с отступами 10 пикселей
            // необходим для выравнивания кнопки
            HBox hbSend = new HBox(10);
            // кнопка равняется по правому краю
            hbSend.setAlignment(Pos.BOTTOM_RIGHT);
            hbSend.getChildren().add(bSend);
            // поместим вспомогалку в сетку
            grid.add(hbSend, 1, 4);

            // метка
            Label lInstall = new Label("Установка сервера:");
            grid.add(lInstall, 0, 5);

            // кнопка запуска сервера
            Button bInstall = new Button("Установить");
            HBox hbInstall = new HBox(10);
            hbInstall.setAlignment(Pos.BOTTOM_RIGHT);
            hbInstall.getChildren().add(bInstall);
            grid.add(hbInstall, 1, 5);

            // метка
            Label lStart = new Label("Запуск сервера:");
            grid.add(lStart, 0, 6);

            // кнопка запуска сервера
            Button bStart = new Button("Запустить");
            HBox hbStart = new HBox(10);
            hbStart.setAlignment(Pos.BOTTOM_RIGHT);
            hbStart.getChildren().add(bStart);
            grid.add(hbStart, 1, 6);

            Button bTest = new Button("Справка");
            final HBox hbTest = new HBox(10);
            hbTest.setAlignment(Pos.BOTTOM_RIGHT);
            hbTest.getChildren().add(bTest);
            grid.add(hbTest, 1, 7);


            ////////////////////////////////
            /// СОБЫТИЯ       ///
            ///////////////////////////////

            // текст оповещения неправильного ввода команды
            final Text actionSend = new Text();
            grid.add(actionSend, 1, 8);

            // событие кнопки, если нажата то выводим оповещение красного цвета
            // событие кнопки "Проверить"
            bSend.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    resultTextField.clear(); //чистим окно результата
                    String text = userTextField.getText(); // получаем команду от клиента
                    try {
                        out.writeUTF(text); //отправляем серверу
                        out.flush(); //прекращаем поток данных
                        text = in.readUTF(); //получаем ответ на запрос
                        resultTextField.setText(text); //выводим в окно результата
                    } catch (IOException e1) {
                        System.out.println("Ошибочка с отправкой/получением команды");
                    }
                    actionSend.setFill(Color.FIREBRICK);
                    actionSend.setText("Введите команду");
                }
            });


            // текст оповещения если кнопка нажата
            final Text actionInstall = new Text();
            grid.add(actionInstall, 1, 7);

            // событие кнопки, если нажата то выводим оповещение красного цвета
            // событие кнопки "Установить"
            bInstall.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    // запуск приложения
                    Runtime runtime = Runtime.getRuntime();
                    Process p = null;
                    String str = "C:/Kursov_Project/rja/bin/InstallApp-NT.bat";
                    try {
                        p = runtime.exec(str);
                    } catch (Exception ee) {
                        System.out.println("Array: " + str);
                    }
                    actionSend.setFill(Color.FIREBRICK);
                    actionSend.setText("Установленно");
                }
            });

            // текст оповещения если кнопка нажата
            final Text actionStart = new Text();
            grid.add(actionStart, 1, 8);

            // событие кнопки, если нажата то выводим оповещение красного цвета
            // событие кнопки "Запустить"
            bStart.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    // запуск внешнего приложения
                    Runtime runtime = Runtime.getRuntime();
                    Process p = null;
                    String s = "c:/rja/bin/StartApp-NT.bat";
                    // exec - выполнение
                    try {
                        p = runtime.exec(s);
                    } catch (Exception ee) {
                        System.out.println("Array: " + s);
                    }
                    actionSend.setFill(Color.FIREBRICK);
                    actionSend.setText("Запущено");

                    resultTextField.setText("OK");

                }
            });
            // событие кнопки, если нажата то выводим оповещение красного цвета
            // событие кнопки "Справка"
            bTest.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {

                    Stage dialogStage = new Stage();
                    dialogStage.setTitle("Справка");
                    dialogStage.showAndWait();

                    actionSend.setFill(Color.FIREBRICK);
                    actionSend.setText("Запущено");
                }
            });

            primaryStage.show(); // запускаем окно

        } catch (Exception ex) {
            System.out.println("Ошибочка! Запустика друже сервер для начала,");
            System.out.println(" а потом и пытай на здоровье.");
        }
    }

    // метод main в JavaFX приложениях не является обязательным
    public static void main(String[] args) {
        launch(args);

    }
}