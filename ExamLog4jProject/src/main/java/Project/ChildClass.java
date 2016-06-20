package Project;

/**
 * Created by Марсель on 19.06.2016.
 */
public class ChildClass extends User {

    public ChildClass(String strRandom) {
        super(strRandom);
    }

    //аннотация, переопределение метода
    @Override
    public void doSomething(String str){
        System.out.println("Имя пользователя : " + str);
    }
}
