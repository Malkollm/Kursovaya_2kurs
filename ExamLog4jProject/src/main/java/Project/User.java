package Project;

public class User {

    //это мы переопределили на ChildClass.doSomething
    public void doSomething(String str){
        System.out.println("Введите имя пользователя : " + str);
    }

    String strRandom; //для получения строки
    //конструктор
    public User(String strRandom) { //информация о логе
        this.strRandom=strRandom; //ссылка на текущий объект
    }

    int randNumbLog; //для получения числа
    public User(int randNumbLog) { //рандомное число запуска программы
        this.randNumbLog=randNumbLog;
    }
}