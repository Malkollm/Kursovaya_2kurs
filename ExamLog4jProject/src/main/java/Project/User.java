package Project;

import java.util.Random;

public class User {
    //Наследование
    String nameUs; //будет передавать имя пользователя
    String lastNameUs;
    public User(String nameUs, String lastNameUs){ //конструктор
        this.nameUs=nameUs; //ссылка на текщий объект
        this.lastNameUs=lastNameUs;
    }

    public String showMeMessage() {
        return "Random number of this log";
    }

    public Integer giveMeASign() {
        Random random = new Random();
        return random.nextInt();
    }
}