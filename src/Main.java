
import java.io.IOException;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.setProperty("sun.java2d.noddraw", "true");
        SQL_Connection sql_conn = new SQL_Connection();
        Connection conn = sql_conn.sql_connection();
        Gui gui = new Gui();
        gui.Login_Frame(conn);
    }
}
