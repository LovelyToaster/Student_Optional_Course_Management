
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.setProperty("sun.java2d.noddraw", "true");
        Gui gui = new Gui();
        gui.Login_Frame();
    }
}
