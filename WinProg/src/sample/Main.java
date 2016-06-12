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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Main extends Application {

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
            // Stage - окно

            // в sample.fxml прописываются компоненты, их свойства
            final Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

            primaryStage.setTitle("Помогалка"); // задаем заголовок окна

            // создаем сцену с заданными шириной и высотой  и связываем ее с окном
            primaryStage.setScene(new Scene(root, 300, 400));

            // гибкая сетка из столбцов и строк
            grid.setAlignment(Pos.CENTER); // сетка в центре
            grid.setHgap(20); //пробелы между строками и столбцами
            grid.setVgap(20);
            grid.setPadding(new Insets(30, 30, 30, 30)); //пространство вокруг сетки по 25 пикселей

            // сцена шириной 300 и высотой 275
            Scene scene = new Scene(grid, 400, 300);
            primaryStage.setScene(scene);

            // объект текст который нельзя изменить
            Text sceneTitle = new Text("Что вы ищете?");
            // задание шрифта/размера/стиля
            sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
            // добавляет объект к сцене
            grid.add(sceneTitle, 0, 0, 2, 1);

            //метка
            Label userName = new Label("Введите запрос");
            grid.add(userName, 0, 1);

            // текстовое поле для ввода команд
            final TextField userTextField = new TextField();
            userTextField.setMinSize(150, 25); //минимальный размер окна
            grid.add(userTextField, 1, 1); //расположение на сцене


            // метка
            Label lposition = new Label("Расположение");
            grid.add(lposition, 0, 2);
            // метка
            final Label lraspologenye = new Label(" - ");
            grid.add(lraspologenye, 1, 2);


            ////////////////////////////////
            /// КНОПКИ      ///////////////
            ///////////////////////////////


            // кнопка проверки команды у сервера
            Button bSend = new Button("Поиск");
            // вспомогательный объект с отступами 10 пикселей
            // необходим для выравнивания кнопки
            HBox hbSend = new HBox(10);
            // кнопка равняется по правому краю
            hbSend.setAlignment(Pos.BOTTOM_RIGHT);
            hbSend.getChildren().add(bSend);
            // поместим вспомогалку в сетку
            grid.add(hbSend, 1, 3);


            // кнопка очистки поля запрос
            Button bClear = new Button("Очистить");
            HBox hbClear = new HBox(10);
            hbClear.setAlignment(Pos.BOTTOM_RIGHT);
            hbClear.getChildren().add(bClear);
            grid.add(hbClear, 1, 4);

           /* // кнопка
            Button bSave = new Button("Сохранить");
            HBox hbSave = new HBox(10);
            hbSave.setAlignment(Pos.BOTTOM_RIGHT);
            hbSave.getChildren().add(bSave);
            grid.add(hbSave, 0, 3);*/

            Button bHelp = new Button("Справка");
            final HBox hbHelp = new HBox(10);
            hbHelp.setAlignment(Pos.BOTTOM_RIGHT);
            hbHelp.getChildren().add(bHelp);
            grid.add(hbHelp, 0, 4);


            /////////////////////////////////
            /// СОБЫТИЯ       //////////////
            ///////////////////////////////


            // кнопка "Проверить"
            bSend.setOnAction(new EventHandler<ActionEvent>() {
                /*Аннотация-о том что мы собираемся переопределить метод базового класса
                * если такогого не окажется то получим предупреждение компилятора о
                * том что переопределение не произошло*/
                @Override
                public void handle(ActionEvent e) {
                    String text = userTextField.getText(); // получаем команду от клиента
                    try {
                        out.writeUTF(text); //отправляем серверу
                        out.flush(); //прекращаем поток данных
                        text = in.readUTF(); //получаем ответ на запрос
                        lraspologenye.setText(text); //выводим результат
                    } catch (IOException e1) {
                        System.out.println("Ошибочка с отправкой/получением команды");
                    }
                }
            });


            // кнопка "Очистить"
            bClear.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    userTextField.clear();
                    lraspologenye.setText(" - ");
                }
            });


            /*bSave.setOnAction(new EventHandler<ActionEvent>() {
                                  @Override
                                  public void handle(ActionEvent e) {
                                      saveToFile(userTextField.getText());
                                  }
                              });*/


                    // событие кнопки "Справка"
                    bHelp.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            Stage dialogStage = new Stage(); //новое окно под справку
                            dialogStage.setTitle("Справка"); //заголовок
                            dialogStage.setMinWidth(500); //размер окна
                            dialogStage.setMinHeight(350);
                            String string = "Основная задача программы: пользователь вводит команду запроса, " +
                                    "\n к примеру Хирургическое отделение " +
                                    "\n и в строке результата ему будет указано как пройти " +
                                    "\n до нужного ему отделения или места в больнице" +
                                    "\n Команды для поиска(выберите нужную и вбейте в поиск): " +
                                    "\n Дата" +
                                    "\n Хирургическое отделение" +
                                    "\n Родильное отделение" +
                                    "\n Онкологическое отделение" +
                                    "\n Кардиологическое отделение" +
                                    "\n Эндоскопическое отделение" +
                                    "\n Стоматологическое отделение" +
                                    "\n Нефрологическое отделение" +
                                    "\n Кризисное отделение" +
                                    "\n Морг" +
                                    "\n Туалет" +
                                    "\n Регистратура";
                            final TextArea textArea = new TextArea(string); //компонент текстовое поле
                            textArea.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14)); //шрифт
                            textArea.setMaxSize(500, 600); //размер компоненты
                            Scene scene = new Scene(textArea); //создаем сцену для выравнивания компоненты
                            dialogStage.setScene(scene);//добавляем сцену в окно
                            dialogStage.showAndWait(); //окно на виду, ожидая закрытия пользователем
                        }
                    });

            primaryStage.show(); // запускаем окно

        } catch (Exception ex) {
            System.out.println("Ошибочка!");
        }
    }

    // метод main в JavaFX приложениях не является обязательным
    public static void main(String[] args) {
        launch(args);

    }
}