
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.setProperty("sun.java2d.noddraw", "true");
        Student stu = new Student();
        Gui gui=new Gui();
        stu.student_out();
        gui.Login_Frame(stu);
    }
}
